update credite_egs.tblBBCasualties set TmpPF=PercentFault;
update credite_egs.tblBBCasualties set TmpPF=100.000 where TmpPF=99.000;

/*--*/

update credite_egs.tblBBCasualties set PercentFault=TmpPF;
