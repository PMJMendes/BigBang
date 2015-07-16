select * from bbleiria.tblBBPolicies

select distinct PolicyNumber from bbleiria.tblBBPolicies
where PolicyNumber in (select PolicyNumber from bbleiria.tblBBPolicies group by PolicyNumber having count(PK)>1)

select * from bbleiria.tblBBPolicies where PolicyNumber not in (select PolicyNumber from bbleiria.tblBBPolicies group by PolicyNumber having count(PK)>1) order by PolicyNumber

select * from SEGEST_SEGAPO

select distinct Numero from SEGEST_SEGAPO
where Numero in (select Numero from SEGEST_SEGAPO group by Numero having count(Numero)>1) order by Numero

select * from SEGEST_SEGAPO where Numero not in (select Numero from SEGEST_SEGAPO group by Numero having count(Numero)>1) order by Numero

select * from bbleiria.tblBBPolicies where PolicyNumber in (select Numero collate database_default from SEGEST_SEGAPO group by Numero having count(Numero)>1) order by PolicyNumber

select c.ShortName, p.PolicyNumber from bbleiria.tblBBPolicies p
inner join bbleiria.tblCompanies c on c.PK=p.FKCompany
left outer join SEGEST_SEGAPO s on right('000000000000000'+s.Numero, 15)=right('000000000000000'+p.PolicyNumber, 15) collate database_default
where s.Numero is null and p.PolicyNumber not in (select PolicyNumber from bbleiria.tblBBPolicies group by PolicyNumber having count(PK)>1)
order by 1, 2

select p.* from bbleiria.tblBBPolicies p
inner join SEGEST_SEGAPO s on right('000000000000000'+s.Numero, 15)=right('000000000000000'+p.PolicyNumber, 15) collate database_default
where p.PolicyNumber not in (select PolicyNumber from bbleiria.tblBBPolicies group by PolicyNumber having count(PK)>1)
and s.Numero not in (select Numero from SEGEST_SEGAPO group by Numero having count(Numero)>1)
order by p.FKCompany, p.PolicyNumber

select c.ShortName, c2.ShortName, p.PolicyNumber from bbleiria.tblBBPolicies p
inner join SEGEST_SEGAPO s on right('000000000000000'+s.Numero, 15)=right('000000000000000'+p.PolicyNumber, 15) collate database_default
inner join bbleiria.tblCompanies c on c.PK=p.FKCompany
inner join SEGEST_SEGCOMP a on a.Codigo=s.Companhia
inner join bbleiria.tblCompanies c2 on c2.MigrationID=a.Codigo
where p.PolicyNumber not in (select PolicyNumber from bbleiria.tblBBPolicies group by PolicyNumber having count(PK)>1)
and s.Numero not in (select Numero from SEGEST_SEGAPO group by Numero having count(Numero)>1)
and (c.MigrationID=a.Codigo
or (c.MigrationID!=a.Codigo
and left(c2.ShortName, 3)=left(c.ShortName, 3)))
order by 1, 2, 3

select p.PolicyNumber, c.ShortName, c2.ShortName from bbleiria.tblBBPolicies p
inner join SEGEST_SEGAPO s on right('000000000000000'+s.Numero, 15)=right('000000000000000'+p.PolicyNumber, 15) collate database_default
inner join bbleiria.tblCompanies c on c.PK=p.FKCompany
inner join SEGEST_SEGCOMP a on a.Codigo=s.Companhia
inner join bbleiria.tblCompanies c2 on c2.MigrationID=a.Codigo
where p.PolicyNumber not in (select PolicyNumber from bbleiria.tblBBPolicies group by PolicyNumber having count(PK)>1)
and s.Numero not in (select Numero from SEGEST_SEGAPO group by Numero having count(Numero)>1)
and c.MigrationID!=a.Codigo
and left(c2.ShortName, 3)!=left(c.ShortName, 3)
order by 2, 3, 1
