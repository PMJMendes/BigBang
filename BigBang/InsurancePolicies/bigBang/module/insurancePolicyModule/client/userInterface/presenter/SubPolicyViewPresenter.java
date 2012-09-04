package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Policy2;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicy.TableSection;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SubPolicyViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT, CANCEL_EDIT, SAVE, INCLUDE_INSURED_OBJECT, CREATE_INSURED_OBJECT, INCLUDE_INSURED_OBJECT_FROM_CLIENT, CREATE_INSURED_OBJECT_FROM_CLIENT, EXCLUDE_INSURED_OBJECT, PERFORM_CALCULATIONS, VALIDATE, TRANSFER_TO_POLICY, CREATE_INFO_OR_DOCUMENT_REQUEST, CREATE_RECEIPT, VOID, DELETE, CREATE_EXPENSE, BACK
	}

	public static interface Display {
		HasEditableValue<SubPolicy> getForm();

		HasValue<Policy2> getParentPolicyForm();

		HasValueSelectables<Contact> getContactsList();

		HasValueSelectables<Document> getDocumentsList();

		HasValueSelectables<ExerciseStub> getExercisesList();

		HasValueSelectables<InsuredObjectStub> getInsuredObjectsList();

		HasValueSelectables<ReceiptStub> getReceiptsList();

		HasValueSelectables<BigBangProcess> getSubProcessesList();

		HasValueSelectables<HistoryItemStub> getHistoryList();

		HasValueSelectables<ExpenseStub> getExpensesList();

		void setSaveModeEnabled(boolean enabled);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		// PERMISSIONS
		void clearAllowedPermissions();

		void allowEdit(boolean allow);

		void allowIncludeInsuredObject(boolean allow);

		void allowCreateInsuredObject(boolean allow);

		void allowIncludeInsuredObjectFromClient(boolean allow);

		void allowCreateInsuredObjectFromClient(boolean allow);

		void allowExcludeInsuredObject(boolean allow);

		void allowPerformCalculations(boolean allow);

		void allowValidate(boolean allow);

		void allowTransferToPolicy(boolean allow);

		void allowCreateInfoOrDocumentRequest(boolean allow);

		void allowCreateReceipt(boolean allow);

		void allowVoid(boolean allow);

		void allowDelete(boolean allow);

		void allowCreateHealthExpense(boolean allow);

		// Table
		HasValue<String> getInsuredObjectFilter();

		HasValue<String> getExerciseFilter();

		TableSection getCurrentTableSectionInfo();

		void setTableSectionInfo(TableSection info);

		Widget asWidget();

	}

	protected Display view;
	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected InsurancePolicyBroker policyBroker;
	protected boolean bound = false;

	public SubPolicyViewPresenter(Display view) {
		this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager
				.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		this.policyBroker = (InsurancePolicyBroker) DataBrokerManager
				.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((UIObject) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		String subPolicyId = parameterHolder.getParameter("subpolicyid");
		subPolicyId = subPolicyId == null ? new String() : subPolicyId;
		String parentPolicyId = parameterHolder.getParameter("policyid");
		parentPolicyId = parentPolicyId == null ? new String() : parentPolicyId;

		if (subPolicyId.isEmpty()) {
			onGetSubPolicyFailed();
		} else if (subPolicyId.equalsIgnoreCase("new")) {
			clearView();
			showCreateSubPolicy(parentPolicyId);
		} else if (this.subPolicyBroker.isTemp(subPolicyId)) {
			showScratchPadSubPolicy(subPolicyId);
		} else {
			showSubPolicy(subPolicyId);
		}
	}

	private void bind() {
		if (bound) {
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<SubPolicyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case EDIT:
					onEdit();
					break;
				case SAVE:
					onSave();
					break;
				case CANCEL_EDIT:
					onCancel();
					break;
				case DELETE:
					onDelete();
					break;
				case VOID:
					onVoid();
					break;
				case CREATE_INSURED_OBJECT:
					onCreateInsuredObject();
					break;
				case CREATE_INSURED_OBJECT_FROM_CLIENT:
					// TODO
					break;
				case INCLUDE_INSURED_OBJECT:
					// TODO
					break;
				case INCLUDE_INSURED_OBJECT_FROM_CLIENT:
					// TODO
					break;
				case EXCLUDE_INSURED_OBJECT:
					// TODO
					break;
				case VALIDATE:
					onValidate();
					break;
				case PERFORM_CALCULATIONS:
					onPerformCalculations();
					break;
				case CREATE_RECEIPT:
					onCreateReceipt();
					break;
				case TRANSFER_TO_POLICY:
					onTransferToPolicy();
					break;
				case CREATE_INFO_OR_DOCUMENT_REQUEST:
					onCreateInfoOrDocumentRequest();
					break;
				case CREATE_EXPENSE:
					onCreateExpense();
					break;
				case BACK:
					onBack();
					break;
				}
			}
		});

		ValueChangeHandler<String> filterChangedHandler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				saveWorkState(new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						subPolicyBroker.getPage(view.getForm().getInfo().id,
								view.getInsuredObjectFilter().getValue(), view
								.getExerciseFilter().getValue(),
								new ResponseHandler<SubPolicy.TableSection>() {

							@Override
							public void onResponse(TableSection response) {
								view.setTableSectionInfo(response);
							}

							@Override
							public void onError(
									Collection<ResponseError> errors) {
								return;
							}
						});
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onResponse(null);
					}
				});
			}
		};
		view.getExerciseFilter().addValueChangeHandler(filterChangedHandler);
		view.getInsuredObjectFilter().addValueChangeHandler(
				filterChangedHandler);

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Object source = event.getSource();
				ValueSelectable<?> selectable = (ValueSelectable<?>) event
						.getFirstSelected();

				if (selectable == null) {
					return;
				}

				Object value = selectable.getValue();

				if (source == view.getContactsList()) {
					showContact((Contact) value);
				} else if (source == view.getDocumentsList()) {
					showDocument((Document) value);
				} else if (source == view.getInsuredObjectsList()) {
					showInsuredObject((InsuredObjectStub) value);
				} else if (source == view.getExercisesList()) {
					showExercise((ExerciseStub) value);
				} else if (source == view.getReceiptsList()) {
					showReceipt((ReceiptStub) value);
				} else if (source == view.getHistoryList()) {
					showHistory((HistoryItemStub) value);
				} else if (source == view.getSubProcessesList()) {
					showSubProcess((BigBangProcess) value);
				} else if (source == view.getExpensesList()) {
					showExpense((ExpenseStub) value);
				}
			}
		};

		view.getContactsList().addSelectionChangedEventHandler(
				selectionChangedHandler);
		view.getDocumentsList().addSelectionChangedEventHandler(
				selectionChangedHandler);
		view.getInsuredObjectsList().addSelectionChangedEventHandler(
				selectionChangedHandler);
		view.getExercisesList().addSelectionChangedEventHandler(
				selectionChangedHandler);
		view.getReceiptsList().addSelectionChangedEventHandler(
				selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(
				selectionChangedHandler);
		view.getExpensesList().addSelectionChangedEventHandler(
				selectionChangedHandler);
		view.getSubProcessesList().addSelectionChangedEventHandler(selectionChangedHandler);

		bound = true;
	}

	protected void showExpense(final ExpenseStub value) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "expense");
				item.setStackParameter("display");
				item.pushIntoStackParameter("display", "search");
				item.setParameter("expenseid", value.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	protected void onCreateExpense() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.pushIntoStackParameter("display", "createexpensesubpolicy");
		item.setParameter("objectid", "new");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void clearView() {
		view.getForm().setValue(null);
	}

	protected void showCreateSubPolicy(final String parentPolicyId) {
		this.subPolicyBroker.openSubPolicyResource(null,
				new ResponseHandler<SubPolicy>() {
			@Override
			public void onResponse(SubPolicy subPolicy) {
				subPolicy.mainPolicyId = parentPolicyId;
				subPolicyBroker.getSubPolicyDefinition(subPolicy,
						new ResponseHandler<SubPolicy>() {

					@Override
					public void onResponse(
							SubPolicy initializedSubPolicy) {
						NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
						item.setParameter("subpolicyid", initializedSubPolicy.id);
						NavigationHistoryManager.getInstance().go(item);
					}

					@Override
					public void onError(
							Collection<ResponseError> errors) {
						onCreateSubPolicyFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateSubPolicyFailed();
			}
		});
	}

	protected void showSubPolicy(String subPolicyId) {
		if (this.subPolicyBroker.isTemp(subPolicyId)) {
			showScratchPadSubPolicy(subPolicyId);
		} else {
			this.subPolicyBroker.getSubPolicy(subPolicyId,
					new ResponseHandler<SubPolicy>() {

				@Override
				public void onResponse(SubPolicy response) {
					showParentPolicy(response.mainPolicyId);

					view.clearAllowedPermissions();

					view.allowDelete(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.DELETE_SUB_POLICY));
					view.allowCreateInsuredObject(subPolicyBroker
							.isTemp(response.id));
					view.allowEdit(subPolicyBroker.isTemp(response.id)
							|| PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EDIT_SUB_POLICY));
					view.allowPerformCalculations(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.PERFORM_CALCULATIONS));
					view.allowValidate(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VALIDATE));
					view.allowTransferToPolicy(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.TRANSFER_TO_POLICY));
					view.allowCreateInfoOrDocumentRequest(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_INFO_OR_DOCUMENT_REQUEST));
					view.allowCreateReceipt(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT));
					view.allowVoid(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VOID));
					view.allowCreateHealthExpense(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_EXPENSE));
					view.getForm().setReadOnly(true);
					view.setSaveModeEnabled(false);

					view.getForm().setValue(response);

				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetSubPolicyFailed();
				}
			});
		}
	}

	protected void showScratchPadSubPolicy(String subPolicyId) {
		if (subPolicyBroker.isTemp(subPolicyId)) {
			this.subPolicyBroker.getSubPolicy(subPolicyId,
					new ResponseHandler<SubPolicy>() {

				@Override
				public void onResponse(SubPolicy response) {
					showParentPolicy(response.mainPolicyId);
					view.clearAllowedPermissions();

					view.allowEdit(true);
					view.setSaveModeEnabled(true);
					view.allowDelete(PermissionChecker
							.hasPermission(
									response,
									BigBangConstants.OperationIds.InsuranceSubPolicyProcess.DELETE_SUB_POLICY));
					view.allowCreateInsuredObject(true);

					view.getForm().setReadOnly(false);

					view.getForm().setValue(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetSubPolicyFailed();
				}
			});
		} else {
			showSubPolicy(subPolicyId);
		}
	}

	protected void onEdit() {
		final SubPolicy subPolicy = view.getForm().getValue();
		if (subPolicy != null) {
			if (!subPolicyBroker.isTemp(subPolicy.id)) {
				makeTemp(subPolicy, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						NavigationHistoryManager.getInstance().reload();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetSubPolicyFailed();
					}
				});
			} else {
				NavigationHistoryManager.getInstance().reload();
			}
		}
		;
	}

	protected void onSave() {
		if(view.getForm().validate()) {
			final SubPolicy subPolicy = view.getForm().getInfo();

			if (subPolicyBroker.isTemp(subPolicy.id)) {
				saveWorkState(new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						subPolicyBroker.commitSubPolicy(subPolicy,
								new ResponseHandler<SubPolicy>() {

							@Override
							public void onResponse(SubPolicy response) {
								onSaveSubPolicySuccess();
								NavigationHistoryItem item = NavigationHistoryManager
										.getInstance().getCurrentState();
								item.setParameter("subpolicyid",
										response.id);
								NavigationHistoryManager.getInstance().go(
										item);
							}

							@Override
							public void onError(
									Collection<ResponseError> errors) {
								onSaveSubPolicyError();
							}
						});
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onSaveSubPolicyError();
					}
				});
			} else {
				makeTemp(subPolicy, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						if (subPolicyBroker.isTemp(subPolicy.id)) {
							onSave();
						} else {
							onError(new String[] {});
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetSubPolicyFailed();
					}
				});
			}
		}
	}

	protected void onCancel() {
		final SubPolicy subPolicy = view.getForm().getValue();

		if (subPolicy != null && subPolicyBroker.isTemp(subPolicy.id)) {
			subPolicyBroker.closeSubPolicyResource(subPolicy.id,
					new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					subPolicyBroker.getSubPolicy(subPolicy.id,
							new ResponseHandler<SubPolicy>() {

						@Override
						public void onResponse(
								SubPolicy response) {
							NavigationHistoryManager
							.getInstance().reload();
						}

						@Override
						public void onError(
								Collection<ResponseError> errors) {
							NavigationHistoryItem item = NavigationHistoryManager
									.getInstance()
									.getCurrentState();
							item.removeParameter("subpolicyid");
							item.popFromStackParameter("display");
							NavigationHistoryManager
							.getInstance().go(item);
						}
					});

				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					NavigationHistoryItem item = NavigationHistoryManager
							.getInstance().getCurrentState();
					item.removeParameter("subpolicyid");
					item.popFromStackParameter("display");
					NavigationHistoryManager.getInstance().go(item);
				}
			});
		}
	}

	protected void saveWorkState(final ResponseHandler<Void> handler) {
		SubPolicy subPolicy = view.getForm().getInfo();
		String subPolicyId = subPolicy == null ? null : subPolicy.id;

		if (subPolicyId != null && subPolicyBroker.isTemp(subPolicyId)) {
			subPolicyBroker.updateSubPolicy(subPolicy,
					new ResponseHandler<SubPolicy>() {

				@Override
				public void onResponse(SubPolicy response) {
					String objectId = view.getInsuredObjectFilter()
							.getValue();
					String exerciseId = view.getExerciseFilter()
							.getValue();
					TableSection page = view
							.getCurrentTableSectionInfo();
					subPolicyBroker
					.saveCoverageDetailsPage(
							response.id,
							objectId,
							exerciseId,
							page,
							new ResponseHandler<SubPolicy.TableSection>() {

								@Override
								public void onResponse(
										TableSection response) {
									handler.onResponse(null);
								}

								@Override
								public void onError(
										Collection<ResponseError> errors) {
									handler.onError(new String[] { new String(
											"Could not save the current table page") });
								}
							});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					handler.onError(new String[] { new String(
							"Could not temporarily save the sub policy") });
				}
			});
		} else {
			handler.onResponse(null);
		}
	}

	protected void onDelete() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.setParameter("show", "deletesubpolicy");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void makeTemp(SubPolicy subPolicy,
			final ResponseHandler<Void> handler) {
		subPolicyBroker.openSubPolicyResource(subPolicy.id,
				new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				handler.onResponse(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(errors);
			}
		});
	}

	protected void onCreateInsuredObject() {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager
						.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "viewsubpolicyobject");
				item.setParameter("objectid", "new");
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(
						new NewNotificationEvent(new Notification("",
								"Não é possível criar unidades de risco."),
								TYPE.ALERT_NOTIFICATION));
			}
		});

	}

	protected void showParentPolicy(String parentPolicyId) {
		if (parentPolicyId == null) {
			onGetParentFailed();
		} else {
			this.policyBroker.getPolicy(parentPolicyId,
					new ResponseHandler<Policy2>() {

				@Override
				public void onResponse(Policy2 response) {
					view.getParentPolicyForm().setValue(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetParentFailed();
				}
			});
		}
	}

	protected void showContact(final Contact contact) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager
						.getInstance().getCurrentState();
				item.setParameter("show", "contactmanagement");
				item.setParameter("ownerid", contact.ownerId);
				item.setParameter("contactownertypeid", contact.ownerTypeId);
				item.setParameter("contactid", contact.id);
				item.setParameter("contactownerid", contact.ownerId);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	protected void showDocument(final Document document) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager
						.getInstance().getCurrentState();
				item.setParameter("show", "documentmanagement");
				item.setParameter("ownerid", document.ownerId);
				item.setParameter("documentownertypeid", document.ownerTypeId);
				item.setParameter("documentid", document.id);
				item.setParameter("documentownerid", document.ownerId);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	protected void showInsuredObject(final InsuredObjectStub insuredObject) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager
						.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "viewsubpolicyobject");
				item.setParameter("objectid", insuredObject.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});

	}

	protected void showExercise(final ExerciseStub exercise) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager
						.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "viewsubpolicyexercise");
				item.setParameter("exerciseid", exercise.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});

	}

	protected void showReceipt(final ReceiptStub receiptItem) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "receipt");
				item.setStackParameter("display");
				item.pushIntoStackParameter("display", "search");
				item.setParameter("receiptid", receiptItem.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	protected void showSubProcess(final BigBangProcess process) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if (process.dataTypeId
						.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)) {
					item.pushIntoStackParameter("display", "viewsubpolicyinforequest");
					item.setParameter("requestid", process.dataId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	protected void showHistory(final HistoryItemStub historyItem) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager
						.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "history");
				item.setParameter("historyownerid",
						view.getForm().getValue().id);
				item.setParameter("historyitemid", historyItem.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void onCreateInfoOrDocumentRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.pushIntoStackParameter("display", "subpolicyinforequest");
		item.setParameter("ownerid", view.getForm().getValue().id);
		item.setParameter("ownertypeid",
				BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onTransferToPolicy() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.setParameter("show", "subpolicytransfertopolicy");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateReceipt() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.pushIntoStackParameter("display", "subpolicycreatereceipt");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onPerformCalculations() {
		subPolicyBroker.executeDetailedCalculations(
				view.getForm().getValue().id, new ResponseHandler<SubPolicy>() {

					@Override
					public void onResponse(SubPolicy response) {
						onExecuteDetailedCalculationsSuccess();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onExecuteDetailedCalculationsFailed();
					}
				});
	}

	private void onValidate() {
		subPolicyBroker.validateSubPolicy(view.getForm().getValue().id,
				new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onValidateSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onValidateFailed(errors);
			}
		});
	}

	private void onVoid() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.setParameter("show", "voidsubpolicy");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onBack(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.removeParameter("subpolicyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetParentFailed() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"Não foi possível obter a Apólice Principal"),
						TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.removeParameter("subpolicyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetSubPolicyFailed() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"Não foi possível obter a Apólice Adesão"),
						TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.removeParameter("subpolicyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateSubPolicyFailed() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"Não foi possível criar a Apólice Adesão"),
						TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.removeParameter("subpolicyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSaveSubPolicySuccess() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"A Apólice Adesão foi guardada com sucesso"),
						TYPE.TRAY_NOTIFICATION));
	}

	private void onSaveSubPolicyError() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"Não foi possível guardar a Apólice Adesão"),
						TYPE.ALERT_NOTIFICATION));
	}

	private void onExecuteDetailedCalculationsFailed() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"Não foi possível executar os cálculos detalhados"),
						TYPE.ALERT_NOTIFICATION));
	}

	private void onExecuteDetailedCalculationsSuccess() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"Os cálculos detalhados foram executados com sucesso"),
						TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	private void onValidateFailed(Collection<ResponseError> errors) {
		StringBuilder message = new StringBuilder("A validação falhou:");
		for (ResponseError error : errors) {
			message.append(error.description.replaceAll("(\r\n|\n)", "<br />"));
		}
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("", message
						.toString()), TYPE.ALERT_NOTIFICATION));
	}

	private void onValidateSuccess() {
		EventBus.getInstance().fireEvent(
				new NewNotificationEvent(new Notification("",
						"A Apólice Adesão foi validada com sucesso"),
						TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

}
