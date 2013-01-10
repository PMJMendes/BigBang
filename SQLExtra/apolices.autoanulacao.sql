update n2 set NodeCount=1
from credite_egs.tblPNNodes n1
inner join credite_egs.tblPNNodes n2 on n2.FKProcess=n1.FKProcess
where n1.FKController='74C8A735-8425-4637-8D61-A03100CEB4B3' and n1.NodeCount>0
and n2.FKController='690D5B0C-17B7-4109-A79D-9FD4010D4BB0' and n2.NodeCount=0

update n2 set NodeCount=1
from credite_egs.tblPNNodes n1
inner join credite_egs.tblPNNodes n2 on n2.FKProcess=n1.FKProcess
where n1.FKController='0B074EE5-F169-4058-AC0B-A031010143BA' and n1.NodeCount>0
and n2.FKController='40EBB8EF-3077-4104-95F4-9FF301308A2B' and n2.NodeCount=0

update n2 set NodeCount=1
from amartins.tblPNNodes n1
inner join amartins.tblPNNodes n2 on n2.FKProcess=n1.FKProcess
where n1.FKController='74C8A735-8425-4637-8D61-A03100CEB4B3' and n1.NodeCount>0
and n2.FKController='690D5B0C-17B7-4109-A79D-9FD4010D4BB0' and n2.NodeCount=0

update n2 set NodeCount=1
from amartins.tblPNNodes n1
inner join amartins.tblPNNodes n2 on n2.FKProcess=n1.FKProcess
where n1.FKController='0B074EE5-F169-4058-AC0B-A031010143BA' and n1.NodeCount>0
and n2.FKController='40EBB8EF-3077-4104-95F4-9FF301308A2B' and n2.NodeCount=0

delete from credite_egs.tblPNNodes
where FKController in ('A1FEEF98-6D5C-42A8-8C64-A03100CEAC96',
'74C8A735-8425-4637-8D61-A03100CEB4B3',
'0AE37B32-73A3-4CA2-9684-A03100CEBF48',
'04C7ED4D-B0EA-43A9-B581-A03101013A7F',
'0B074EE5-F169-4058-AC0B-A031010143BA',
'55DF188D-036D-4171-AE61-A0310101544C')

delete from credite_egs.tblPNSteps
where FKOperation in ('C5762CA8-E911-468F-BC4D-A03100CE29AF',
'9B392086-8DEE-4402-BD5A-A03100CE6B71',
'12CD7FE7-5386-4499-B0A6-A0310100C4F8',
'12F09413-EBE1-4D65-8B61-A0310100F4CB')

delete from amartins.tblPNNodes
where FKController in ('A1FEEF98-6D5C-42A8-8C64-A03100CEAC96',
'74C8A735-8425-4637-8D61-A03100CEB4B3',
'0AE37B32-73A3-4CA2-9684-A03100CEBF48',
'04C7ED4D-B0EA-43A9-B581-A03101013A7F',
'0B074EE5-F169-4058-AC0B-A031010143BA',
'55DF188D-036D-4171-AE61-A0310101544C')

delete from amartins.tblPNSteps
where FKOperation in ('C5762CA8-E911-468F-BC4D-A03100CE29AF',
'9B392086-8DEE-4402-BD5A-A03100CE6B71',
'12CD7FE7-5386-4499-B0A6-A0310100C4F8',
'12F09413-EBE1-4D65-8B61-A0310100F4CB')
