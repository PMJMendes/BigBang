select * from bbleiria.tblBBPolicies where PolicyNumber in ('10922324', '10268785', '6900101189') order by PolicyNumber

select * from SEGEST_SEGAPO where Numero in ('6746339') order by Numero

select * from bbleiria.tblBBPolicies p
left outer join SEGEST_SEGAPO s on right('000000000000000'+s.Numero, 15)=right('000000000000000'+p.PolicyNumber, 15) collate database_default
where s.Numero is null

select c.MigrationID, a.Codigo, count(p.PK) from bbleiria.tblBBPolicies p
inner join SEGEST_SEGAPO s on right('000000000000000'+s.Numero, 15)=right('000000000000000'+p.PolicyNumber, 15) collate database_default
inner join bbleiria.tblCompanies c on c.PK=p.FKCompany
inner join SEGEST_SEGCOMP a on a.Codigo=s.Companhia
where p.PolicyNumber not in ('10922324', '10268785', '6900101189', '6746339')
group by c.MigrationID, a.Codigo order by 3 desc

select * from SEGEST_SEGCOMP where Codigo in (1, 2, 3, 10, 14, 19, 24, 32, 33)
