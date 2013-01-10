update credite_egs.tblBBCasualties set TmpPF=PercentFault;
update credite_egs.tblBBCasualties set TmpPF=100.000 where TmpPF=99.000;

update amartins.tblBBCasualties set TmpPF=PercentFault;
update amartins.tblBBCasualties set TmpPF=100.000 where TmpPF=99.000;


/*--*/

update credite_egs.tblBBCasualties set PercentFault=TmpPF;

update amartins.tblBBCasualties set PercentFault=TmpPF;
