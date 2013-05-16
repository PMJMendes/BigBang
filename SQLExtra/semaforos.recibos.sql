update credite_egs.tblBBReceipts set FKStatus='37C3A6F7-A579-4CD2-842E-A02000C337AA';

update r set FKStatus='C359D3FF-9032-4B4D-8C34-A02000C3403D'
from credite_egs.tblPNLogs l
inner join credite_egs.tblBBReceipts r on r.FKProcess=l.FKProcess
where l.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
and l.Undone=0;

update r set FKStatus='ABCA5C34-078C-4382-8FCB-A17800FE83EC'
from credite_egs.tblBBReceipts r
where r.FKProcess in
(select FKProcess
from credite_egs.tblPNLogs l
where l.FKOperation in ('A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57',
'08799EDB-B874-49D5-B987-A01300CCAC59', '002A20EF-A7B3-4DE7-B62C-A01300CC5450')
and l.Undone=0)
and r.FKProcess not in
(select FKProcess
from credite_egs.tblPNSteps s
where s.FKOperation in ('A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57',
'08799EDB-B874-49D5-B987-A01300CCAC59', '002A20EF-A7B3-4DE7-B62C-A01300CC5450')
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5');

update r set FKStatus='767EB803-6669-47EF-8649-A02000C3491F'
from credite_egs.tblPNSteps s
inner join credite_egs.tblBBReceipts r on r.FKProcess=s.FKProcess
where s.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5';

update r set FKStatus='8C43ED6B-A047-4549-9922-A02000C35098'
from credite_egs.tblPNSteps s
inner join credite_egs.tblBBReceipts r on r.FKProcess=s.FKProcess
where s.FKOperation in ('4CAD549D-2903-4A42-9767-A01300C98B51', 'EE580116-A45A-43AE-A2ED-A01300CBFDD9')
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5';

update r set FKStatus='B86D383A-70E9-4B03-87C7-A02000C35B32'
from credite_egs.tblPNSteps s
inner join credite_egs.tblBBReceipts r on r.FKProcess=s.FKProcess
where s.FKOperation in ('563A630A-276A-48E8-96D3-A01300C9AF24', '04C24A5B-5CED-4CC3-A983-A01300CC84EC')
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5';

update r set FKStatus='ABCA5C34-078C-4382-8FCB-A17800FE83EC'
from credite_egs.tblPNLogs l
inner join credite_egs.tblBBReceipts r on r.FKProcess=l.FKProcess
where l.FKOperation in ('A34C65D4-0B2A-4083-BB16-A01300D3D013', '36A6571E-0601-47CE-A53E-A15600BA69D5')
and l.Undone=0;

update amartins.tblBBReceipts set FKStatus='37C3A6F7-A579-4CD2-842E-A02000C337AA';

update r set FKStatus='C359D3FF-9032-4B4D-8C34-A02000C3403D'
from amartins.tblPNLogs l
inner join amartins.tblBBReceipts r on r.FKProcess=l.FKProcess
where l.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
and l.Undone=0;

update r set FKStatus='ABCA5C34-078C-4382-8FCB-A17800FE83EC'
from amartins.tblBBReceipts r
where r.FKProcess in
(select FKProcess
from amartins.tblPNLogs l
where l.FKOperation in ('A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57',
'08799EDB-B874-49D5-B987-A01300CCAC59', '002A20EF-A7B3-4DE7-B62C-A01300CC5450')
and l.Undone=0)
and r.FKProcess not in
(select FKProcess
from amartins.tblPNSteps s
where s.FKOperation in ('A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57',
'08799EDB-B874-49D5-B987-A01300CCAC59', '002A20EF-A7B3-4DE7-B62C-A01300CC5450')
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5');

update r set FKStatus='767EB803-6669-47EF-8649-A02000C3491F'
from amartins.tblPNSteps s
inner join amartins.tblBBReceipts r on r.FKProcess=s.FKProcess
where s.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5';

update r set FKStatus='8C43ED6B-A047-4549-9922-A02000C35098'
from amartins.tblPNSteps s
inner join amartins.tblBBReceipts r on r.FKProcess=s.FKProcess
where s.FKOperation in ('4CAD549D-2903-4A42-9767-A01300C98B51', 'EE580116-A45A-43AE-A2ED-A01300CBFDD9')
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5';

update r set FKStatus='B86D383A-70E9-4B03-87C7-A02000C35B32'
from amartins.tblPNSteps s
inner join amartins.tblBBReceipts r on r.FKProcess=s.FKProcess
where s.FKOperation in ('563A630A-276A-48E8-96D3-A01300C9AF24', '04C24A5B-5CED-4CC3-A983-A01300CC84EC')
and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5';

update r set FKStatus='ABCA5C34-078C-4382-8FCB-A17800FE83EC'
from amartins.tblPNLogs l
inner join amartins.tblBBReceipts r on r.FKProcess=l.FKProcess
where l.FKOperation in ('A34C65D4-0B2A-4083-BB16-A01300D3D013', '36A6571E-0601-47CE-A53E-A15600BA69D5')
and l.Undone=0;
