update n set NodeCount=1
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNNodes n on n.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and n.FKController='5AF77A4E-857F-4957-835E-A01300D6B99A';

update s set FKLevel='7925EF60-80FC-4EA2-96A0-9EB1007EA1FF'
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNSteps s on s.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and s.FKOperation='6BC653FD-3265-4A3C-9307-A01300C8B2BC';
