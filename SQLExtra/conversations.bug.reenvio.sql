update credite_egs.tblPNSteps set FKLevel='7925EF60-80FC-4EA2-96A0-9EB1007EA1FF'
where FKOperation='62217CB6-5B54-4E43-870C-A118011AECE2' and FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' and FKProcess in
(select FKProcess from credite_egs.tblConversations where PK in
(select FKConversation from credite_egs.tblMessages where FKDirection='4AFA1A58-3BB6-4F55-9CF6-A11800F3142A'));

update credite_egs.tblPNNodes set NodeCount=1
where FKController='6C12D74B-475B-46C9-87F4-A11801309C81' and NodeCount=0 and FKProcess in
(select FKProcess from credite_egs.tblConversations where PK in
(select FKConversation from credite_egs.tblMessages where FKDirection='4AFA1A58-3BB6-4F55-9CF6-A11800F3142A'));

update amartins.tblPNSteps set FKLevel='7925EF60-80FC-4EA2-96A0-9EB1007EA1FF'
where FKOperation='62217CB6-5B54-4E43-870C-A118011AECE2' and FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' and FKProcess in
(select FKProcess from amartins.tblConversations where PK in
(select FKConversation from amartins.tblMessages where FKDirection='4AFA1A58-3BB6-4F55-9CF6-A11800F3142A'));

update amartins.tblPNNodes set NodeCount=1
where FKController='6C12D74B-475B-46C9-87F4-A11801309C81' and NodeCount=0 and FKProcess in
(select FKProcess from amartins.tblConversations where PK in
(select FKConversation from amartins.tblMessages where FKDirection='4AFA1A58-3BB6-4F55-9CF6-A11800F3142A'));
