update n set NodeCount=0
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNNodes n on n.FKProcess=r.FKProcess
where r.BIsInternal=1
and n.FKController in ('FF6DDAAC-9699-477C-AA8E-A01300D659FD', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3');

update n set NodeCount=1
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNNodes n on n.FKProcess=r.FKProcess
and n.FKController='C581CE0F-42EC-4EFB-AF69-A12A01196B69'
left outer join credite_egs.tblPNLogs l on l.FKProcess=r.FKProcess
and l.FKOperation='E870B538-B7AE-4B33-B13E-A12A011BD386'
where r.BIsInternal=1 and l.PK is null;

update n set NodeCount=0
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNNodes n on n.FKProcess=r.FKProcess
and n.FKController='C581CE0F-42EC-4EFB-AF69-A12A01196B69'
inner join credite_egs.tblPNLogs l on l.FKProcess=r.FKProcess
and l.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
where r.BIsInternal=1 and l.TStamp<'2012-11-04';

update credite_egs.tblPNSteps set FKLevel=o.FKDefaultLevel
from credite_egs.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
where o.PK in ('994D421F-E414-41EF-8D02-9F8A00EEE620', 'E99A9029-BBF0-4E76-AC49-A01300C4FB8C',
'D6A00787-5509-4F47-A681-A01300C56C1F', '5C10D8AF-8862-45B3-9CCC-A01300C79228', '2815599A-E132-4805-9D34-A01300C9DF96',
'002A20EF-A7B3-4DE7-B62C-A01300CC5450', '11397F84-2A3B-4365-9F1D-A0BD01205EE0', 'B0D0475F-9628-46A7-9C41-A10E01300E46',
'E870B538-B7AE-4B33-B13E-A12A011BD386');

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation in ('994D421F-E414-41EF-8D02-9F8A00EEE620', 'E99A9029-BBF0-4E76-AC49-A01300C4FB8C',
'D6A00787-5509-4F47-A681-A01300C56C1F', '5C10D8AF-8862-45B3-9CCC-A01300C79228', '2815599A-E132-4805-9D34-A01300C9DF96',
'002A20EF-A7B3-4DE7-B62C-A01300CC5450', '11397F84-2A3B-4365-9F1D-A0BD01205EE0', 'B0D0475F-9628-46A7-9C41-A10E01300E46',
'E870B538-B7AE-4B33-B13E-A12A011BD386')
and PK in
(select s.PK
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join credite_egs.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);

update n set NodeCount=0
from amartins.tblBBReceipts r
inner join amartins.tblPNNodes n on n.FKProcess=r.FKProcess
where r.BIsInternal=1
and n.FKController in ('FF6DDAAC-9699-477C-AA8E-A01300D659FD', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3');

update n set NodeCount=1
from amartins.tblBBReceipts r
inner join amartins.tblPNNodes n on n.FKProcess=r.FKProcess
and n.FKController='C581CE0F-42EC-4EFB-AF69-A12A01196B69'
left outer join amartins.tblPNLogs l on l.FKProcess=r.FKProcess
and l.FKOperation='E870B538-B7AE-4B33-B13E-A12A011BD386'
where r.BIsInternal=1 and l.PK is null;

update n set NodeCount=0
from amartins.tblBBReceipts r
inner join amartins.tblPNNodes n on n.FKProcess=r.FKProcess
and n.FKController='C581CE0F-42EC-4EFB-AF69-A12A01196B69'
inner join amartins.tblPNLogs l on l.FKProcess=r.FKProcess
and l.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
where r.BIsInternal=1 and l.TStamp<'2012-11-04';

update amartins.tblPNSteps set FKLevel=o.FKDefaultLevel
from amartins.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
where o.PK in ('994D421F-E414-41EF-8D02-9F8A00EEE620', 'E99A9029-BBF0-4E76-AC49-A01300C4FB8C',
'D6A00787-5509-4F47-A681-A01300C56C1F', '5C10D8AF-8862-45B3-9CCC-A01300C79228', '2815599A-E132-4805-9D34-A01300C9DF96',
'002A20EF-A7B3-4DE7-B62C-A01300CC5450', '11397F84-2A3B-4365-9F1D-A0BD01205EE0', 'B0D0475F-9628-46A7-9C41-A10E01300E46',
'E870B538-B7AE-4B33-B13E-A12A011BD386');

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation in ('994D421F-E414-41EF-8D02-9F8A00EEE620', 'E99A9029-BBF0-4E76-AC49-A01300C4FB8C',
'D6A00787-5509-4F47-A681-A01300C56C1F', '5C10D8AF-8862-45B3-9CCC-A01300C79228', '2815599A-E132-4805-9D34-A01300C9DF96',
'002A20EF-A7B3-4DE7-B62C-A01300CC5450', '11397F84-2A3B-4365-9F1D-A0BD01205EE0', 'B0D0475F-9628-46A7-9C41-A10E01300E46',
'E870B538-B7AE-4B33-B13E-A12A011BD386')
and PK in
(select s.PK
from amartins.tblPNSteps s
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join amartins.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);
