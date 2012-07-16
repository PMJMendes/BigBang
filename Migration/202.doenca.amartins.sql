/** 708 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'DB605A61-8AC7-427A-8A1C-9F340151C1A9' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=708 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6BAF112B-C5E9-4FF0-A0C7-9F9100FBC050' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5091EE55-C18C-4F43-B402-9F9100FBCF5B' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '486CC859-D2A1-4C70-88E8-9F9100FBFDE4' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '25209EFA-0A9B-4127-BB67-9F9100FBD701' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '82C211F8-FDA5-4D7F-A580-9F9100FC104E' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '97508E04-2328-4AE2-A848-9F9100FBE230' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3043ECBD-DAF2-4933-B227-9F9100FC1836' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2A884F86-5752-4367-9091-9F9100FBC6C5' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '386596D0-0C43-4181-ACF7-9F9100FBE9A7' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2EE268DD-F91E-4C1C-AD99-9F9100FBF54B' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EB60AC74-F6C4-4615-880A-9F9100FC07B4' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBSubPolicies (PK, SubPolicyNumber, FKProcess, FKClient, BeginDate, EndDate, FKFractioning, SubPolicyNotes, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ss.apolice SubPolicyNumber, null FKProcess, c.PK FKClient, ss.datini BeginDate, ss.datfim EndDate, f.PK FKFractioning, substring(ss.observ, 1, 250) SubPolicyNotes,
case ss.situacao when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(ss.moeda, 1) when 2 then ss.vpremio else ss.vpremio/200.482 end, 2) Premium, ss.DocuShare DShareFolder, ss.MigrationID MigrationID
from amartins..empresa.apolice ss
inner join amartins.tblbbclients c on c.MigrationID=ss.cliente
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = ss.fpagamento COLLATE DATABASE_DEFAULT
where ss.ramo=10708 and ss.texto2=708 and (ss.situacao in ('P', 'N') or (ss.situacao in ('A', 'U') and (ss.datfim is null or ss.datfim>'2009-12-31')));

insert into amartins.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
b.PK FKSubPolicy, z.FKCoverage, z.BPresent
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages z on z.FKPolicy=p.PK
where s.ramo=708 and ss.ramo=10708;

insert into amartins.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, '2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' FKTaxAsSubList
from (select distinct case ltrim(rtrim(isnull(texto2, ''))) when '' then null else
case left(texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end v
from amartins..empresa.objectos where ramo=10708) x
where x.v is not null and x.v not in
(select ItemValue from amartins.tblPolicyValueItems where FKTaxAsSubList='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F');

insert into amartins.tblSubPolicyObjects (PK, ObjName, FKSubPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex,
DateOfBirth, ClientNumberI, InsurerIDI, MigrationAux)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
o.Texto1 ObjName, b.PK FKSubPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType, c.Address1 Address1, c.Address2 Address2, c.FKZipCode FKZipCode,
o.ValidoDesde InclusionDate, o.ValidoAte ExclusionDate, case when left(isnull(o.Texto2, ''), 1)='T' then c.FiscalNumber else NULL end FiscalNumberI, s.PK FKSex, NULL DateOfBirth,
case when left(isnull(o.Texto2, ''), 1)='T' then c.ClientNumber else NULL end ClientNumberI, NULL InsurerIDI, o.NumObjecto MigrationAux
from amartins..empresa.objectos o
inner join amartins..empresa.apolice ss on ss.cliente=o.cliente and ss.apolice=o.apolice and ss.ramo=o.ramo and ss.comseg=o.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbclients c on c.PK=b.FKClient
left outer join bigbang.tblSex s on left(s.SexName, 1) COLLATE DATABASE_DEFAULT =left(o.Texto3, 1) COLLATE DATABASE_DEFAULT
where o.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, ss.PK FKSubPolicy, '2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.NumObjecto=z.MigrationAux
left outer join amartins.tblPolicyValueItems v on v.ItemValue=case ltrim(rtrim(isnull(o.texto2, ''))) when '' then null else
case left(o.texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when o.texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F')
and s.ramo=10708 and s.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, 'EBBFEF55-3334-4A99-AE4E-9FE200CA7D72' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '8C6A4578-57AB-45FB-B6B2-A05B0107E6CD' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '6958DBA5-7A21-48F7-BE75-A05B0107D24D' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'CD45F751-7A25-4BA9-99B4-9FE200CAAD4B' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Titular'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '030B85CD-71C5-4891-9113-9FE200CACE3C' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Cônjuge'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '48BD5524-3DB0-4F82-8BEA-9FE200CD6620' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Maior'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '17B5E1DA-D77A-43A5-AB12-9FE200CD8792' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Menor'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'D56812DE-E5F5-4C20-B4B5-9F9601575FB8' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Titular'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '20CD0AC8-9FFA-4928-852B-9F9601577C06' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Cônjuge'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'CF516E0B-C36F-476C-BFCC-9FE200CA297D' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Maior'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '591DCF99-34E8-4211-A17C-9F960157A4F7' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Menor'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, 'F686E0E4-1517-41BB-97D7-9FE200CDB525' FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies ss
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theCount Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, sum(cast(v.Value AS INTEGER)) theCount
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('CD45F751-7A25-4BA9-99B4-9FE200CAAD4B', '030B85CD-71C5-4891-9113-9FE200CACE3C', '48BD5524-3DB0-4F82-8BEA-9FE200CD6620', '17B5E1DA-D77A-43A5-AB12-9FE200CD8792')
and ss.ramo=10708 and s.ramo=708
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('CD45F751-7A25-4BA9-99B4-9FE200CAAD4B', '030B85CD-71C5-4891-9113-9FE200CACE3C', '48BD5524-3DB0-4F82-8BEA-9FE200CD6620', '17B5E1DA-D77A-43A5-AB12-9FE200CD8792')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, max(v.Value) theMax
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('D56812DE-E5F5-4C20-B4B5-9F9601575FB8', '20CD0AC8-9FFA-4928-852B-9F9601577C06', 'CF516E0B-C36F-476C-BFCC-9FE200CA297D', '591DCF99-34E8-4211-A17C-9F960157A4F7')
and ss.ramo=10708 and s.ramo=708
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('D56812DE-E5F5-4C20-B4B5-9F9601575FB8', '20CD0AC8-9FFA-4928-852B-9F9601577C06', 'CF516E0B-C36F-476C-BFCC-9FE200CA297D', '591DCF99-34E8-4211-A17C-9F960157A4F7')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F686E0E4-1517-41BB-97D7-9FE200CDB525' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5C5EDCE3-60BB-4671-B46F-A05B0103E044' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='610ACE63-2D2B-450F-8F8F-A05B01042690' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='28905900-639E-48B6-8A80-A05B01049ADB' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0364A72E-6D63-483A-A45D-A05B0104BAFA' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D06601AC-3C1D-49BD-82C7-A05B0104E054' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5F442716-CCFF-404F-817A-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='361B6E77-8AB4-424B-AF8E-A05B01040339' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='585629AC-D46B-4AB1-BECB-A05B01046B65' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='93A105C5-934D-45C1-9D3B-A05B01051873' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FDDFE8E8-28DD-4765-A567-A05B010564BF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='115A69CF-1822-4380-AF37-A05B0105B724' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CE76D738-2AFC-461C-8E87-A05B010593BF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D89206E2-458E-4340-8938-A05B0105FE69' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D7F26719-6401-44F5-B076-A05B0105D434' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='15E73725-E734-457D-AE6F-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CE2F7E84-0DCD-431B-8E73-A05B01053785' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C739EA9E-DCF3-4C33-B866-A05B01062933' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='709FDB03-2DDB-415F-8AFF-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E4A1F7E8-C781-46C7-A8B1-A05B010651AF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A91BA07B-10E1-4B36-BD5C-A05B0106EDB6' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9470215D-A199-4BCA-B883-A05B01077D3C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C42AEDD3-1DA6-464A-B47C-A05B0107A6D1' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E9A5AC18-EFE5-4024-B836-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EEABAA50-AFD7-4EB0-BF56-A05B01066F47' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C46854F3-B0F5-4049-949F-A05B01085305' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='164EA58C-F708-4D5A-AA00-A05B0108A5DB' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='38695493-C873-4427-8D67-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6ECCF2BF-BE9E-4894-80EA-A05B010888AF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='959A17D9-7A12-43D7-8FB3-A05B0108E3E7' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EFB8B428-6EE1-4E2D-9FA9-A05B010919BB' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1A82F860-58FB-4F18-A376-A05B010988B4' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D558BA3-DEF0-4B11-9080-A05B0109A721' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='376A72D0-184B-4D48-BA26-A05B0109F3C3' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='63B50259-BA53-4870-9B95-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F6A8668C-FE8A-4D6F-9A59-A05B0108FD9E' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='37489DD2-CA1C-4E1B-8E40-A05B01095A1A' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D0578CA-97ED-4AB6-A463-A05B010B3969' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2B2B82A6-BE5A-48A9-BE2F-A05B010B7482' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='49995F58-FF82-4035-90DB-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F2DDA4E3-56B6-45C8-868F-A05B010B58F6' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='57A71B80-9D24-4CF1-ACED-A05B010BC952' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BAFEA98D-C217-44D7-9822-A05B010C0DD3' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D669D1B9-0BF4-4CE5-95C2-A05B010C3C55' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2CDDE0B2-8695-41EC-AB91-A05B010C5C11' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5C914CEA-A8E3-4508-B3FE-A05B010C7C42' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EE6E630C-8B0C-426F-A280-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E4AC0A85-D6DA-46C4-B102-A05B010BE743' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.Value Value, b.PK FKSubPolicy, v.FKField FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyValues v on v.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.PK=v.FKField
inner join bigbang.tblBBCoverages c on c.PK=t.FKCoverage
where c.BHeader=0
and s.ramo=708 and ss.ramo=10708;

/** 718 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=718 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '45823667-05AC-4F75-8F73-9F9100FC2F2E' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F36EF742-51E3-494D-8AA6-9F9100FC3C1E' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '212FBAE7-B7DD-412F-90A0-9F9100FC68DC' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F607DB83-88D8-4997-8130-9F9100FC43BF' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6345B6E1-E79B-45CF-80CA-9F9100FC7E66' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1F0977A5-C0EF-477F-9210-9F9100FC4AD0' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'AB99B436-91CB-4DC3-B0C7-9F9100FC8637' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '746EE69B-F8EA-4F02-B566-9F9100FC34DF' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'AD416A46-6C74-4A54-9FBB-9F9100FC5214' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A8158091-7DFE-4E89-A3B4-9F9100FC5B6E' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '902909C4-985D-491F-BA0A-9F9100FC760E' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBSubPolicies (PK, SubPolicyNumber, FKProcess, FKClient, BeginDate, EndDate, FKFractioning, SubPolicyNotes, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ss.apolice SubPolicyNumber, null FKProcess, c.PK FKClient, ss.datini BeginDate, ss.datfim EndDate, f.PK FKFractioning, substring(ss.observ, 1, 250) SubPolicyNotes,
case ss.situacao when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(ss.moeda, 1) when 2 then ss.vpremio else ss.vpremio/200.482 end, 2) Premium, ss.DocuShare DShareFolder, ss.MigrationID MigrationID
from amartins..empresa.apolice ss
inner join amartins.tblbbclients c on c.MigrationID=ss.cliente
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = ss.fpagamento COLLATE DATABASE_DEFAULT
where ss.ramo=10708 and ss.texto2=718 and (ss.situacao in ('P', 'N') or (ss.situacao in ('A', 'U') and (ss.datfim is null or ss.datfim>'2009-12-31')));

insert into amartins.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
b.PK FKSubPolicy, z.FKCoverage, z.BPresent
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages z on z.FKPolicy=p.PK
where s.ramo=718 and ss.ramo=10708;

insert into amartins.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, 'AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' FKTaxAsSubList
from (select distinct case ltrim(rtrim(isnull(texto2, ''))) when '' then null else
case left(texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end v
from amartins..empresa.objectos where ramo=10708) x
where x.v is not null and x.v not in
(select ItemValue from amartins.tblPolicyValueItems where FKTaxAsSubList='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF');

insert into amartins.tblSubPolicyObjects (PK, ObjName, FKSubPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex,
DateOfBirth, ClientNumberI, InsurerIDI, MigrationAux)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
o.Texto1 ObjName, b.PK FKSubPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType, c.Address1 Address1, c.Address2 Address2, c.FKZipCode FKZipCode,
o.ValidoDesde InclusionDate, o.ValidoAte ExclusionDate, case when left(isnull(o.Texto2, ''), 1)='T' then c.FiscalNumber else NULL end FiscalNumberI, s.PK FKSex, NULL DateOfBirth,
case when left(isnull(o.Texto2, ''), 1)='T' then c.ClientNumber else NULL end ClientNumberI, NULL InsurerIDI, o.NumObjecto MigrationAux
from amartins..empresa.objectos o
inner join amartins..empresa.apolice ss on ss.cliente=o.cliente and ss.apolice=o.apolice and ss.ramo=o.ramo and ss.comseg=o.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbclients c on c.PK=b.FKClient
left outer join bigbang.tblSex s on left(s.SexName, 1) COLLATE DATABASE_DEFAULT =left(o.Texto3, 1) COLLATE DATABASE_DEFAULT
where o.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, ss.PK FKSubPolicy, 'AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.NumObjecto=z.MigrationAux
left outer join amartins.tblPolicyValueItems v on v.ItemValue=case ltrim(rtrim(isnull(o.texto2, ''))) when '' then null else
case left(o.texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when o.texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF')
and s.ramo=10708 and s.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '8118E542-ECB6-4FB0-BC0C-9FE200EC19FF' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '6A9B4E45-BE0B-4D39-BBF1-A05B01240F6E' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '563187CF-0064-4449-AB75-A05B01240F6E' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '7B3DF1B7-5162-4DF1-BDCE-9FE200EDF0B0' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Titular'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '17798432-8AF2-4135-B792-9FE200F23697' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Cônjuge'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'F3C6A12C-1EDC-4D73-BF1D-9FE200F25296' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Maior'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'B38F365D-0828-4E56-9471-9FE200F5443B' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Menor'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '6CCA6707-6835-48F2-B000-9F960157C8AF' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Titular'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '88F74A16-1A9C-4B63-B635-9F960157E5FD' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Cônjuge'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '35A7C9CA-E949-4297-B5AB-9FE200EA2C8A' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Maior'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'C1C6B0C1-4120-41D5-8F32-9F960157FC5B' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Menor'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '5ECAC9CB-5A5D-4651-B9A1-9FE200F578C8' FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies ss
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theCount Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, sum(cast(v.Value AS INTEGER)) theCount
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('7B3DF1B7-5162-4DF1-BDCE-9FE200EDF0B0', '17798432-8AF2-4135-B792-9FE200F23697', 'F3C6A12C-1EDC-4D73-BF1D-9FE200F25296', 'B38F365D-0828-4E56-9471-9FE200F5443B')
and ss.ramo=10708 and s.ramo=718
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('7B3DF1B7-5162-4DF1-BDCE-9FE200EDF0B0', '17798432-8AF2-4135-B792-9FE200F23697', 'F3C6A12C-1EDC-4D73-BF1D-9FE200F25296', 'B38F365D-0828-4E56-9471-9FE200F5443B')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, max(v.Value) theMax
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('6CCA6707-6835-48F2-B000-9F960157C8AF', '88F74A16-1A9C-4B63-B635-9F960157E5FD', '35A7C9CA-E949-4297-B5AB-9FE200EA2C8A', 'C1C6B0C1-4120-41D5-8F32-9F960157FC5B')
and ss.ramo=10708 and s.ramo=718
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('6CCA6707-6835-48F2-B000-9F960157C8AF', '88F74A16-1A9C-4B63-B635-9F960157E5FD', '35A7C9CA-E949-4297-B5AB-9FE200EA2C8A', 'C1C6B0C1-4120-41D5-8F32-9F960157FC5B')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '5ECAC9CB-5A5D-4651-B9A1-9FE200F578C8' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C2320EA7-837E-4948-84C7-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FDFE5828-6A8B-411A-9B09-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='03459036-7A3F-4293-A63D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D022BEB-D96E-42D3-A867-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ACA75A0A-0099-4AA4-83A1-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C0DBB220-EF4F-46B1-BB40-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7E129ACA-B333-4F61-A8B0-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B7989DE6-3664-4295-A44D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8B2C4A1A-242B-40BA-B868-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D5182C54-44EA-43E2-99AF-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='33010D4F-BF1A-4DF8-B010-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5AC5E573-F1EF-418E-9AD4-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1950FB13-43EF-46ED-9FB7-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FA044DDA-5544-4E96-A855-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D75E5DDF-AC6F-4B08-87A8-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EE51B266-5C8E-490D-B87D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B44AE800-74C2-4433-B668-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0C03DEC3-0076-4B52-91FE-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='286C8180-4313-467C-ABDB-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E0EDEF7B-E98E-4442-AD7E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A0EE188D-1ECD-421C-BD15-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D8DA5B8D-4C25-4A99-9673-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='29BF3B05-D7CA-4BB7-B6A7-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A42759DC-87ED-4CCF-AA87-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='52A8B818-5227-4DDC-B18B-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D4AF214B-8F2A-4511-A614-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B6282A6A-A17C-46A9-BB1E-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FABEAB38-D2A2-4BC0-AB12-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BC3A2B43-8AE9-4292-9844-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0300F72E-55ED-404E-BF05-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6213502B-B5C0-4422-9C6D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F749AEB5-B4B8-45EF-A91A-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2C8F5E4E-6167-401D-B66D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DC4F8EB2-8F7B-46E8-9708-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C7BA2642-79F1-4F98-8866-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2A30A0F9-CD22-4CA4-AA64-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0CD192C5-FED6-4032-8CF8-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8875CCBC-A7B8-4542-9D23-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9BED185F-04D1-4E68-8966-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7C9F7246-055B-438A-A32A-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='87374767-7952-4900-81B5-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ABCA35D7-86C9-4644-BC03-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CCCF397D-626E-4D83-8FB8-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='55788785-726F-43C8-834E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='99FC80B4-CA85-49B4-9309-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='246BD4C1-BB03-4968-A539-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E24C7ACC-5FB9-4713-BA35-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.Value Value, b.PK FKSubPolicy, v.FKField FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyValues v on v.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.PK=v.FKField
inner join bigbang.tblBBCoverages c on c.PK=t.FKCoverage
where c.BHeader=0
and s.ramo=718 and ss.ramo=10708;

/** 728 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'BB50ACAA-F557-4249-934F-9F340151D334' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=728 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '538A5E80-E43D-42AC-A303-9F9100FC960C' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '9076D1C2-90E6-49B6-820B-9F9100FCA321' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E47ABFD8-2C44-4130-B3EA-9F9100FCC990' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F006A858-4377-4979-94B3-9F9100FCAB24' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '128DBA1E-73B2-4ACE-8327-9F9100FCDAE9' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1D9EE6CD-F646-4D82-8F64-9F9100FCB22C' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CC2471E4-7EDF-4290-B670-9F9100FCEBA7' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '29B4953D-F07E-4F2E-AA90-9F9100FC9BB3' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '64772CFA-55E7-4BE5-826D-9F9100FCB8CC' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '70D081FD-BE0E-45F3-ABB6-9F9100FCC19E' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4CF5D75F-503E-4A9C-A899-9F9100FCD259' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBSubPolicies (PK, SubPolicyNumber, FKProcess, FKClient, BeginDate, EndDate, FKFractioning, SubPolicyNotes, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ss.apolice SubPolicyNumber, null FKProcess, c.PK FKClient, ss.datini BeginDate, ss.datfim EndDate, f.PK FKFractioning, substring(ss.observ, 1, 250) SubPolicyNotes,
case ss.situacao when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(ss.moeda, 1) when 2 then ss.vpremio else ss.vpremio/200.482 end, 2) Premium, ss.DocuShare DShareFolder, ss.MigrationID MigrationID
from amartins..empresa.apolice ss
inner join amartins.tblbbclients c on c.MigrationID=ss.cliente
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = ss.fpagamento COLLATE DATABASE_DEFAULT
where ss.ramo=10708 and ss.texto2=728 and (ss.situacao in ('P', 'N') or (ss.situacao in ('A', 'U') and (ss.datfim is null or ss.datfim>'2009-12-31')));

insert into amartins.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
b.PK FKSubPolicy, z.FKCoverage, z.BPresent
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages z on z.FKPolicy=p.PK
where s.ramo=728 and ss.ramo=10708;

insert into amartins.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, '0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' FKTaxAsSubList
from (select distinct case ltrim(rtrim(isnull(texto2, ''))) when '' then null else
case left(texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end v
from amartins..empresa.objectos where ramo=10708) x
where x.v is not null and x.v not in
(select ItemValue from amartins.tblPolicyValueItems where FKTaxAsSubList='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD');

insert into amartins.tblSubPolicyObjects (PK, ObjName, FKSubPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex,
DateOfBirth, ClientNumberI, InsurerIDI, MigrationAux)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
o.Texto1 ObjName, b.PK FKSubPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType, c.Address1 Address1, c.Address2 Address2, c.FKZipCode FKZipCode,
o.ValidoDesde InclusionDate, o.ValidoAte ExclusionDate, case when left(isnull(o.Texto2, ''), 1)='T' then c.FiscalNumber else NULL end FiscalNumberI, s.PK FKSex, NULL DateOfBirth,
case when left(isnull(o.Texto2, ''), 1)='T' then c.ClientNumber else NULL end ClientNumberI, NULL InsurerIDI, o.NumObjecto MigrationAux
from amartins..empresa.objectos o
inner join amartins..empresa.apolice ss on ss.cliente=o.cliente and ss.apolice=o.apolice and ss.ramo=o.ramo and ss.comseg=o.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbclients c on c.PK=b.FKClient
left outer join bigbang.tblSex s on left(s.SexName, 1) COLLATE DATABASE_DEFAULT =left(o.Texto3, 1) COLLATE DATABASE_DEFAULT
where o.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, ss.PK FKSubPolicy, '0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.NumObjecto=z.MigrationAux
left outer join amartins.tblPolicyValueItems v on v.ItemValue=case ltrim(rtrim(isnull(o.texto2, ''))) when '' then null else
case left(o.texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when o.texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD')
and s.ramo=10708 and s.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '65B0A61E-50EA-457C-85B3-9FE200F5FE04' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '7E11E7A5-BDB6-45B5-84F0-A05B01240F6E' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '245F6A71-CE08-487F-99E5-A05B01240F6E' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'A1A36173-CF8E-4084-B0E2-9FE200F64CB1' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Titular'
and s.ramo=10708 and s.texto2=728
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '1D303F55-13AB-4371-8E97-9FE200F66993' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Cônjuge'
and s.ramo=10708 and s.texto2=728
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '65E94A73-99A9-41BC-A32F-9FE200F68633' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Filho Maior'
and s.ramo=10708 and s.texto2=728
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'EC8F7D75-DE0E-466F-9F29-9FE200F69FC6' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Filho Menor'
and s.ramo=10708 and s.texto2=728
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '5F1CD0AD-D797-48B3-9F44-9F9601582245' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Titular'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=728
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'C7270847-253B-4168-94B0-9F9601583E07' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Cônjuge'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=728
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'A2DD65FD-BBBD-464B-A025-9FE200F5BC71' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Filho Maior'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=728
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '933F52F3-B5E6-423E-BA9B-9F9601585865' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD' and x.ItemValue='Filho Menor'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=728
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=728;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '5C7861EC-55B1-46DD-9157-9FE200F6B9FB' FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies ss
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=728;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theCount Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, sum(cast(v.Value AS INTEGER)) theCount
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('A1A36173-CF8E-4084-B0E2-9FE200F64CB1', '1D303F55-13AB-4371-8E97-9FE200F66993', '65E94A73-99A9-41BC-A32F-9FE200F68633', 'EC8F7D75-DE0E-466F-9F29-9FE200F69FC6')
and ss.ramo=10708 and s.ramo=728
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('A1A36173-CF8E-4084-B0E2-9FE200F64CB1', '1D303F55-13AB-4371-8E97-9FE200F66993', '65E94A73-99A9-41BC-A32F-9FE200F68633', 'EC8F7D75-DE0E-466F-9F29-9FE200F69FC6')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, max(v.Value) theMax
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('5F1CD0AD-D797-48B3-9F44-9F9601582245', 'C7270847-253B-4168-94B0-9F9601583E07', 'A2DD65FD-BBBD-464B-A025-9FE200F5BC71', '933F52F3-B5E6-423E-BA9B-9F9601585865')
and ss.ramo=10708 and s.ramo=728
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('5F1CD0AD-D797-48B3-9F44-9F9601582245', 'C7270847-253B-4168-94B0-9F9601583E07', 'A2DD65FD-BBBD-464B-A025-9FE200F5BC71', '933F52F3-B5E6-423E-BA9B-9F9601585865')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '5C7861EC-55B1-46DD-9157-9FE200F6B9FB' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='08118119-3873-469E-A94C-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='618D6670-E16F-4ADC-B3C4-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FA37ACBD-F8FC-4901-A184-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='49BBC49C-54F4-4871-92E9-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CB31BA11-C025-47BE-9CFD-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5824EA36-48E2-4F2F-B79B-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='15591DA8-9C38-4110-B99E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8C72BD57-0D5C-4ADB-8A05-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='10C7940A-B60F-4D56-B349-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F817BF03-E71B-441A-A0C1-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='67C5E0D1-37C6-4619-8BEC-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='22E3BEEE-A52B-432D-A442-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CF436AFF-8C31-42B3-808B-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='65F46FA5-22E2-4F60-BA01-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4FF7DD7F-AB8B-4046-888C-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C8520B7D-589E-47AD-A6C2-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2F2FC261-E602-44E6-BAF2-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='64612F0E-B9FC-4D0B-8178-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C2FF0086-BC9B-45EA-80E7-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F7A4B98A-720A-4298-9AE3-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C9D7AF47-77CD-447D-9BCF-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7F844818-3C97-4938-9DB3-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F54B5788-9C26-4756-AFB8-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BCBCDF9F-2F0E-4423-BEF5-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='21D143DB-4692-4F5E-BD52-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AB785CB4-39B3-46D9-A2E2-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AEC47A7F-9C71-4236-BBA5-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CE927157-74E5-4760-B929-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7E3C7361-0955-4106-962E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='37A8BA31-8EBF-4D1F-B21E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D7FCF0CC-8B78-4755-9B88-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F7BD138D-5AF4-44BC-97A9-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D24AC1AF-A15F-43D9-8AF3-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='632AAA50-265A-4103-ABCE-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E355FC60-70B7-4C1E-8A94-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5588CF2A-578C-4443-9689-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='62774383-7375-47ED-A556-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1C6BF13E-8DF8-4D03-BBC4-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CD4D68FE-D3B2-4CB2-83AE-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FC5C9886-C8C9-4EF6-9411-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='809DA8BD-66C7-4B8A-A8A0-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8DA0ACA8-63AC-444C-BEE9-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='66785A9A-2D4C-49F6-A666-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='971ADBBE-7F7E-42D3-A3F9-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3EAC6F0F-A992-483D-895E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1396490A-F884-46D8-BDDE-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C568EB7D-4F0F-4275-95EC-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.Value Value, b.PK FKSubPolicy, v.FKField FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyValues v on v.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.PK=v.FKField
inner join bigbang.tblBBCoverages c on c.PK=t.FKCoverage
where c.BHeader=0
and s.ramo=728 and ss.ramo=10708;

/** 710 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'BB50ACAA-F557-4249-934F-9F340151D334' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=710 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '93CF04E9-8966-436F-9FD0-9F9100FD05A9' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8813E06E-47EF-4045-A518-9F9100FD15A4' FKCoverage,
case when cv.FKCob is null then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '36ECE754-5AA7-48EB-973D-9F9100FD3D13' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '05CAB911-48E9-4163-BA86-9F9100FD1D4A' FKCoverage,
case when cv.FKCob is null then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=8
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2454B340-04ED-475E-97D8-9F9100FD4FE3' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '416992A3-83F2-4A03-BE3D-9F9100FD24CA' FKCoverage,
case when cv.FKCob is null then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=3
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DE5C910E-0DAD-4068-867B-9F9100FD59AE' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D6D2A150-07DC-4D90-BD05-9F9100FD0C99' FKCoverage,
case when cv1.FKCob is null and cv2.FKCob is null then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
left outer join amartins..empresa.saudeporapolice cv1 on cv1.cliente=s.cliente and cv1.apolice=s.apolice and cv1.ramo=s.ramo and cv1.comseg=s.comseg and cv1.FKCob=9
left outer join amartins..empresa.saudeporapolice cv2 on cv2.cliente=s.cliente and cv2.apolice=s.apolice and cv2.ramo=s.ramo and cv2.comseg=s.comseg and cv2.FKCob=10
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5466E21A-F8C7-4601-A822-9F9100FD2C18' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'FF6932CF-D874-4E3B-86CE-9F9100FD348C' FKCoverage,
case when cv.FKCob is null then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=4
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '272FEA4D-5433-48DF-8F68-9F9100FD46E3' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBSubPolicies (PK, SubPolicyNumber, FKProcess, FKClient, BeginDate, EndDate, FKFractioning, SubPolicyNotes, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ss.apolice SubPolicyNumber, null FKProcess, c.PK FKClient, ss.datini BeginDate, ss.datfim EndDate, f.PK FKFractioning, substring(ss.observ, 1, 250) SubPolicyNotes,
case ss.situacao when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(ss.moeda, 1) when 2 then ss.vpremio else ss.vpremio/200.482 end, 2) Premium, ss.DocuShare DShareFolder, ss.MigrationID MigrationID
from amartins..empresa.apolice ss
inner join amartins.tblbbclients c on c.MigrationID=ss.cliente
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = ss.fpagamento COLLATE DATABASE_DEFAULT
where ss.ramo=10710 and ss.texto2=710 and (ss.situacao in ('P', 'N') or (ss.situacao in ('A', 'U') and (ss.datfim is null or ss.datfim>'2009-12-31')));

insert into amartins.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
b.PK FKSubPolicy, z.FKCoverage, z.BPresent
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages z on z.FKPolicy=p.PK
where s.ramo=710 and ss.ramo=10710;

insert into amartins.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select * from
(select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, '2F143086-7F99-4C04-B124-9FE200F7625B' FKTaxAsSubList
from (select distinct case ltrim(rtrim(isnull(texto2, ''))) when '' then null else
case left(texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' when 'E' then 'Cônjuge' when 'M' then 'Cônjuge' else
case when texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end v
from amartins..empresa.objectos where ramo=10710) x
union all
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Filho Maior' ItemValue, '2F143086-7F99-4C04-B124-9FE200F7625B' FKTaxAsSubList) a
where a.ItemValue is not null and a.ItemValue not in
(select ItemValue from amartins.tblPolicyValueItems where FKTaxAsSubList='2F143086-7F99-4C04-B124-9FE200F7625B');

set language portuguese;
insert into amartins.tblSubPolicyObjects (PK, ObjName, FKSubPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex,
DateOfBirth, ClientNumberI, InsurerIDI, MigrationAux)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
o.Texto1 ObjName, b.PK FKSubPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType, NULL Address1, NULL Address2, NULL FKZipCode, o.ValidoDesde InclusionDate,
o.ValidoAte ExclusionDate, NULL FiscalNumberI, s.PK FKSex,
case isdate(replace(o.Texto4, '-', '/')) when 1 then cast(replace(o.Texto4, '-', '/') as datetime) else NULL end DateOfBirth,
NULL ClientNumberI, NULL InsurerIDI, o.NumObjecto MigrationAux
from amartins..empresa.objectos o
inner join amartins..empresa.apolice ss on ss.cliente=o.cliente and ss.apolice=o.apolice and ss.ramo=o.ramo and ss.comseg=o.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbclients c on c.PK=b.FKClient
left outer join bigbang.tblSex s on left(s.SexName, 1) COLLATE DATABASE_DEFAULT =left(o.Texto3, 1) COLLATE DATABASE_DEFAULT
where o.ramo=10710 and ss.texto2=710;
set language us_english;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, ss.PK FKSubPolicy, '2F143086-7F99-4C04-B124-9FE200F7625B' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.NumObjecto=z.MigrationAux
left outer join amartins.tblPolicyValueItems v on v.ItemValue=case ltrim(rtrim(isnull(o.texto2, ''))) when '' then null else
case left(o.texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' when 'E' then 'Cônjuge' when 'M' then 'Cônjuge' else
case when o.texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='2F143086-7F99-4C04-B124-9FE200F7625B')
and s.ramo=10710 and s.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, 'C27777E1-D2C5-4AEE-A324-9FE200F777DD' FKField, z.PK FKObject, NULL FKExercise
from amartins.tblSubPolicyObjects z
inner join amartins.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10710 and s.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '6C3C83BB-F58A-4A13-ABE7-9FE200F78F2A' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Titular'
and s.ramo=10710 and s.texto2=710
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '4F2BAAD0-2CE1-4A2E-86BE-9FE200F7A8BA' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Cônjuge'
and s.ramo=10710 and s.texto2=710
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '3A71CEAF-2575-42ED-9E66-9FE200F7C208' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Filho Maior'
and s.ramo=10710 and s.texto2=710
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '9A57A2B6-274F-49E2-8F66-9FE200F7DA26' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Filho Menor'
and s.ramo=10710 and s.texto2=710
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '6C6DB25D-D65B-4E24-8022-9F96015895E7' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Titular'
and o.Texto5 is not null
and s.ramo=10710 and s.texto2=710
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'DDFE3B7D-63C1-49BF-B9EE-9F960158B2D7' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Cônjuge'
and o.Texto5 is not null
and s.ramo=10710 and s.texto2=710
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'E8BE9944-565F-4643-8AE8-9FE200F744BA' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Filho Maior'
and o.Texto5 is not null
and s.ramo=10710 and s.texto2=710
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '848CD03A-86FA-4B18-B435-9F960158C848' FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from amartins.tblBBSubPolicies ss
inner join amartins.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join amartins.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join amartins.tblPolicyValueItems x on x.PK=v.Value
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2F143086-7F99-4C04-B124-9FE200F7625B' and x.ItemValue='Filho Menor'
and o.Texto5 is not null
and s.ramo=10710 and s.texto2=710
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join amartins..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10710 and ss.texto2=710;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '1900305E-D7FC-47D5-820F-9FE200F7FAC3' FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies ss
inner join amartins..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10710 and s.texto2=710;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theCount Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, sum(cast(v.Value AS INTEGER)) theCount
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('6C3C83BB-F58A-4A13-ABE7-9FE200F78F2A', '4F2BAAD0-2CE1-4A2E-86BE-9FE200F7A8BA', '3A71CEAF-2575-42ED-9E66-9FE200F7C208', '9A57A2B6-274F-49E2-8F66-9FE200F7DA26')
and ss.ramo=10710 and s.ramo=710
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('6C3C83BB-F58A-4A13-ABE7-9FE200F78F2A', '4F2BAAD0-2CE1-4A2E-86BE-9FE200F7A8BA', '3A71CEAF-2575-42ED-9E66-9FE200F7C208', '9A57A2B6-274F-49E2-8F66-9FE200F7DA26')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, max(v.Value) theMax
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join amartins.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join amartins.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('6C6DB25D-D65B-4E24-8022-9F96015895E7', 'DDFE3B7D-63C1-49BF-B9EE-9F960158B2D7', 'E8BE9944-565F-4643-8AE8-9FE200F744BA', '848CD03A-86FA-4B18-B435-9F960158C848')
and ss.ramo=10710 and s.ramo=710
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('6C6DB25D-D65B-4E24-8022-9F96015895E7', 'DDFE3B7D-63C1-49BF-B9EE-9F960158B2D7', 'E8BE9944-565F-4643-8AE8-9FE200F744BA', '848CD03A-86FA-4B18-B435-9F960158C848')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '1900305E-D7FC-47D5-820F-9FE200F7FAC3' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='9A0C4B64-81C7-4B4D-8FD0-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Franquia Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='E4CE7B42-8A55-4BF3-B25A-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='8DBF9ED5-6BC4-4D06-857C-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='60CE6402-054D-4B90-B524-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='0EEF76DC-F750-4A1B-85F0-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Percentagem Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='B6A9112D-951C-4800-A5CB-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'5016B49D-5AC4-42C6-9CDA-A04D0117E74A' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='B036D4A5-B84A-4F90-B63F-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=1
where t.pk='83A12DB7-004B-4E35-8F58-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='EC11EFD6-98F6-47F7-BD5F-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Franquia Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='632A1035-6E70-464F-BD0E-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='310ECB76-50E1-435F-A393-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='24DE2F23-4A57-4922-AFBE-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='98BBE2F7-1C6E-402C-B15E-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='6C564ADD-AF06-4196-842E-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Percentagem Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='ED828129-C55A-4B8A-8499-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'5016B49D-5AC4-42C6-9CDA-A04D0117E74A' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=2
where t.pk='DA0E64DF-29CD-4332-B187-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2697C7D7-940C-4AAD-BE36-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='80EB95F6-6829-4F3E-8FF0-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=8
where t.pk='1FD35D67-A2CC-4F1D-AF51-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Franquia Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=8
where t.pk='79A6E04A-E0D4-498C-B24A-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=8
where t.pk='2E31E26D-A68A-47E0-B0FD-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=8
where t.pk='CFD3586F-6D0F-4ECC-B449-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Percentagem Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=8
where t.pk='070F9ED7-E039-4A86-A361-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'5016B49D-5AC4-42C6-9CDA-A04D0117E74A' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=8
where t.pk='019181FE-09E6-4903-B9E8-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='070C4A6D-A63B-4E12-85A0-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6B20FDD5-4F2F-4258-8690-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=3
where t.pk='91D10270-7E2C-4D0A-AB6A-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Franquia Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=3
where t.pk='A334021D-FA8C-4D70-8811-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Percentagem Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=3
where t.pk='1295AB5E-1F26-4868-A6FD-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'5016B49D-5AC4-42C6-9CDA-A04D0117E74A' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=3
where t.pk='DD7F8585-D219-42A6-8073-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=9
where t.pk='63B9D01C-B6C9-4ADD-893A-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=9
where t.pk='7207F9AE-1242-46A3-841E-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=10
where t.pk='480C3031-36C5-49B3-9282-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=9
where t.pk='81F3E0A5-7046-461A-91C1-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=11
where t.pk='559606EC-2BB7-4B29-8CD1-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Percentagem Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=9
where t.pk='A680C8B8-48A8-4918-8BC8-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'5016B49D-5AC4-42C6-9CDA-A04D0117E74A' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=9
where t.pk='49F92AE5-1352-463E-B44E-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=9
where t.pk='BA3C63D9-72E5-4BD7-A145-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CEF8BF5B-6B35-477F-94BF-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='945F0CA3-7C03-41FA-BF98-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='108E1AF1-CB4C-496D-BD60-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='958A3EAD-1B74-4369-99F3-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=4
where t.pk='1BCF9DD2-39BC-45BC-AE04-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Franquia Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=4
where t.pk='2AF24543-1429-4374-8DA5-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=5
where t.pk='F9BD9515-BF58-4A95-B1FE-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=6
where t.pk='A977522D-08E3-459A-8F66-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Cobertura Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=7
where t.pk='70E24A7E-06DF-4583-8351-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cv.Percentagem Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=4
where t.pk='3147A02D-3D76-4B71-8967-A06000E4816C' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'5016B49D-5AC4-42C6-9CDA-A04D0117E74A' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join amartins..empresa.saudeporapolice cv on cv.cliente=s.cliente and cv.apolice=s.apolice and cv.ramo=s.ramo and cv.comseg=s.comseg and cv.FKCob=4
where t.pk='EEB31E31-BFE2-47AA-BE0C-A05B01298583' and c.BPresent=1
and p.FKSubLine='BB50ACAA-F557-4249-934F-9F340151D334';

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.Value Value, b.PK FKSubPolicy, v.FKField FKField, NULL FKObject, NULL FKExercise
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyValues v on v.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.PK=v.FKField
inner join bigbang.tblBBCoverages c on c.PK=t.FKCoverage
where c.BHeader=0
and s.ramo=710 and ss.ramo=10710;

/** 705 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=705 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'BB5C7FF1-78B8-4E99-86E3-9F9100FD6CA8' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5B4F49E2-E70C-4F04-AF43-9F9100FD78CC' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '510B19FD-C7DE-482E-A9DB-9F9100FD9CA9' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7B9ECEE1-B84F-44B7-A70A-9F9100FD7EC2' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DBA54160-177F-443B-84AF-9F9100FDAF46' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F95693A4-5A7F-4088-8DBB-9F9100FD8642' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '54748891-5ACA-4B50-B0D1-9F9100FDCC06' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2CEC2563-0099-4CF6-A76A-9F9100FD7182' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7D54F404-4710-458C-A2EF-9F9100FD8D61' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '033BD33C-65B3-47FB-8EBC-9F9100FD95CC' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '27B5802D-46E7-4938-9D47-9F9100FDA673' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 Value, p.PK FKPolicy, 'A9DB3E35-014B-4227-AF33-9FE200F9008F' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '71C0CF9C-708B-424D-A783-9FE200F91EA5' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '13B596A7-2D07-46E4-A3F0-9FE200F940C2' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, '379E59B2-171A-4E3E-A091-9FE200F95919' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '85438D80-76EA-464F-99A6-9F960158FA52' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'B06A6DFC-2CE4-47B5-9264-9F9601591A37' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '4375B767-61AE-43AA-8785-9FE200F87A80' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'C6C4542E-553A-42F8-B197-9F960159429C' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'E753D357-BBBD-4200-97E9-9FE200F96FC3' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4D86DC12-D86C-411D-8816-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='51B1B784-54B5-4AD5-8446-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2336925B-F195-413E-943D-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B65F0091-479A-4082-8757-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BDBFFD06-70C3-4905-B6DC-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C425CC77-5B8D-4865-88E0-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8A4A01E9-32BD-4989-9064-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0F3FFF95-7513-4BFF-A38B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B2BDBF9C-D2F0-44A2-B53B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4C345602-D20F-4A2A-B3A7-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2397CAA7-762C-4ECC-AFA8-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E8D89D6D-3E5B-420B-BFE9-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2BF48243-0126-42EA-9FC4-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5FA613A9-DCA0-408A-8785-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D0B92E80-7E1B-4B80-A950-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2FAE6992-9B08-4D33-9B96-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F6AF9619-1245-419F-98B4-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='51D6899B-B262-473A-96F3-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CA61E5E8-B3A1-4C13-9463-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D5A7A8E3-862A-41D1-8CFC-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8054CDC2-F785-4B93-A1CD-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='01788A78-5E1B-4DB6-A169-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='93D8BBED-BF42-4A4A-B270-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='723E155F-3EF8-42A2-A92D-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='817B7A5C-86E3-4345-8DF6-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FED26832-B2FC-489B-872C-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DF969230-D139-451E-A207-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2A2CBC3F-D53A-468B-B18F-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='148DFC3E-F6CE-427A-86B6-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='53FC25C3-8704-4EBD-8CAA-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0B00EFD5-0832-4091-9A9B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5887B801-35E2-4D9E-B474-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CA0F1CD3-64F5-4B23-B806-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='807DB7B3-B7F6-4454-B124-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9D54E985-1537-4374-AD03-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9CA3B422-4A9F-44C0-9678-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5A5ED05D-A7F8-4E7F-AC49-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AC72DB25-BB59-4588-9A8D-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D7425066-757D-4341-A5CB-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E5A1C530-FE10-492A-8542-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='65926A54-2E11-4731-B85D-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3833797F-8FF3-4E0C-8352-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='69E48736-30E6-4FAA-96AA-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='69E9894C-DA28-47C4-8F9B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7761A6C5-ED25-4B7D-8A94-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D895D085-1460-4A02-90A9-A06000E4816C' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2CEB6DEB-D79F-4E56-893B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0';

/** 715 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'CD456E4C-1ED7-4819-AB22-9EE901198248' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=715 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '86158294-BC3E-480B-BC97-9F9100FDE030' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B263B84B-0B36-4B81-B32D-9F9100FDEB48' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2697EC08-0FDD-4174-9B96-9F9100FE4CCE' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1B929B6D-E471-45AA-B533-9F9100FDF217' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '012F7718-4904-4AAD-AAEB-9F9100FE602B' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '324F3B1E-208F-4A23-B485-9F9100FE31B8' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '17E05A70-937B-4E08-A607-9F9100FE69E2' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D572D20D-0E39-4C66-AF1A-9F9100FDE588' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'BB2E9DB8-3AAF-438B-9C35-9F9100FE3A7C' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DB9447DE-C7D7-41B9-8C64-9F9100FE44E6' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5BE8E894-40B6-454D-AEE9-9F9100FE570C' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 Value, p.PK FKPolicy, '5395D1AD-12DD-429D-9682-9FE200FAA46B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '67A4D15B-F546-4798-ADE2-9FE200FAC23F' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '1BF676CB-6E3C-4F6A-A9D3-9FE200FADAA4' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, 'D0FA31BC-E7A3-4305-A62B-9FE200FAEFDD' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '594966F0-36A6-474E-9E80-9F96015976EA' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '24C80107-3AFC-48C6-AAAB-9F9601598DB6' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '47398504-BB20-4568-A09E-9FE200F9FF66' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'C18B1ACC-DAAC-4E2B-995F-9F960159A0F4' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F1CEA9C0-05BF-49D6-A0B2-9FE200FB0683' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8115AE56-A57A-4A18-96D7-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='77387789-6105-43DB-AAAE-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='45EBB507-8EBC-431A-B3CA-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A522E4FF-7935-4ED9-B1CA-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A95ACAEA-CD47-4C5B-96A9-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='940FB819-591C-4306-B4CF-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E4AFAA4A-1C3F-4286-A357-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3AD9CCE4-1348-4D4C-A83D-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CE4BAB71-482F-4C35-AE4D-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6C647979-EF05-4B0F-9593-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='38A17ACF-D08D-49C8-9BCE-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='965E2E31-8148-4D0B-BD46-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C12852E8-17C8-4EAF-958B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B1DBED11-704D-463F-9425-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3D271E64-CBCB-4A5E-88DC-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F179C84D-F095-45FA-97E0-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F3BE472C-8EC8-49D8-BFB6-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='92120217-A12C-443A-A5C7-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0ACF86AF-2437-4C88-BF73-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0C1FE2C5-5241-48F9-B5BA-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='08500850-C795-43ED-9C57-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='81722EFE-050F-4D57-9D48-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A54516C3-BAA5-45D8-970C-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CABEAB79-DE3E-4E25-985B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='48AFFD0B-FF8E-4F4D-ABDB-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7B48C6F1-05D1-4A32-AD8E-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C0276569-CA99-43EC-A58E-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5ECC9F04-7E98-40C0-AADD-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FCB8D922-081B-4C87-B318-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='106736FA-5AC5-49B3-B741-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='80D27C42-9B6A-4DE8-99E4-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8F2569D6-5184-48DB-8C98-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FD373B95-2186-43A4-B7D3-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B2027ED2-CB41-4239-8AD3-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9E4F7AAA-847A-440D-B89B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ADE55F99-73DF-4362-ADF8-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='01B4D3AF-9455-4F4E-9952-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='06FD7425-CE56-4F9D-B953-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D978822-7EF0-4266-AEB0-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5B92392F-0E16-41E1-A9DE-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AB7A684F-95C3-4DE6-944E-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A225B1E0-17F3-4E4D-B31D-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='075B66BD-32D5-4957-B3FF-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DD8F4FA5-D453-4228-B3F4-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EA41EB4E-4368-4AD6-875E-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1DA4BAEC-F493-4F23-96B0-A06000E4816C' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='293F9A1A-2E5A-4445-9D25-A05B0126400D' and c.BPresent=1
and p.FKSubLine='CD456E4C-1ED7-4819-AB22-9EE901198248';

/** 725 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=725 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '99F35BF2-9C15-4166-9B22-9F9100FE7FD4' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B71A0880-9A77-477B-9531-9F9100FE8D84' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '93EF0AB4-F4B7-4D48-9490-9F9100FEB28A' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6D2AB153-039A-4584-96A2-9F9100FE945C' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C9994D93-3485-498D-A345-9F9100FEC82C' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F50DF3D3-F77E-491F-BD26-9F9100FE9B5F' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B308D706-4CB6-479B-AEB8-9F9100FED1EC' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3CE58987-C7B1-49DB-AAAB-9F9100FE8596' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C9FB1272-1CD9-43D1-9819-9F9100FEA25F' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8AE1588C-881B-48A6-8419-9F9100FEAB04' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F5521D83-8B3F-4573-A77C-9F9100FEBE45' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 Value, p.PK FKPolicy, 'A5E004F1-3D6E-4E7A-BFBA-9FE200FBF2FC' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '3CC7BB11-9429-4C52-84C5-9FE200FC0C2A' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '3DFF9B4B-1AB7-41CC-BD67-9FE200FC28EE' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, 'DD8ECF3C-56DC-4270-9853-9FE200FC8BE3' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '0B60D535-1861-420F-9946-9F960159B739' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '6F7A09B2-BF05-4D40-966C-9F960159CFB7' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F71B1C80-8E43-4985-8595-9FE200FB6CFC' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'D5DFE8CC-AC01-4E26-A82E-9F960159E195' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'A4CDB098-14D4-47AF-8841-9FE200FCA0D1' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C882FEFE-C26E-4601-8E37-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='53A010D0-515D-429E-9132-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F54BC6C0-48BE-4A2C-BC63-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3D858FC4-173C-4DF7-B875-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CBA0100D-815F-464B-8409-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='81DA8BCE-6CAC-44A3-9EF8-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4FD619CF-A561-40DC-9314-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='69E07DB0-D883-473C-89DB-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0F6314B8-81D9-428A-B30E-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='197B65EE-3D23-449D-944A-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EC18250B-D91B-44F0-A48A-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C595C775-4A82-4737-8745-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6A51D1C3-7DF2-4823-B3B8-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0DFA0256-6474-4A63-9B71-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0AF16908-1FB5-4655-A772-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='63B3DEC1-4DC3-427F-BC06-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C2AC3BB9-46A5-4A97-9377-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AD02AED9-F5C3-4B32-B272-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0CAF6545-B0BF-4103-B56A-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9A90CCF1-9410-46DD-B8F5-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='70726900-8C49-4D63-92C9-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A208A048-5E54-48A1-8746-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='68C259EF-D4E4-41A0-92E8-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='663ABF64-EA2A-4691-8FDE-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A790856E-1E34-40E8-B343-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8AF2855D-8D99-4939-B166-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='482DB0AE-0E2B-460D-9306-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='890E28E0-C9AD-414B-B2C0-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BB643180-C1CC-499D-967B-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0EBC8A20-856A-4E01-9E3F-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9086A5E6-E3A8-41AC-8611-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5FBD3816-8927-4040-9053-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6D660A59-C223-479D-8844-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8A09CB4B-2AE3-467F-9592-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FFA7DA07-5641-450D-ACB8-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CF73431B-3709-4B99-9E86-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D727D77D-36F1-4843-AFB5-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1AF62919-CA23-43A1-A0F0-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7FA5EB1B-8941-406F-A55B-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A2B333BD-EE47-49D5-9932-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='75FD9AE4-46B0-4693-BDE9-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C3E7B26B-9127-4208-A240-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D2CE8ADD-1493-4A6F-8CCB-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CF318264-A658-4BF8-BFE9-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1A547209-1C5F-43BB-9169-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E21BA794-2B48-452D-9A57-A06000E4816C' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8E095A31-20D5-4365-845A-A05B0126400D' and c.BPresent=1
and p.FKSubLine='C8E5724B-4BD2-4AAF-ADAE-9EE901197B82';

/** 703 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '1B70989B-1D54-408E-BDF7-9EE90119749F' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=703 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2A3482A8-D87C-4FFD-8ACE-9F9100FEE4C6' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EC823B27-6B82-49A3-B38C-9F9100FEF2D9' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'FC69D7A0-6497-4FD6-AA4B-9F9100FF162B' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '05642DCD-3757-4121-8F37-9F9100FEF97D' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D014DEEF-2C61-4953-BA9C-9F9100FF2A0F' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1E784212-3503-4F38-9A0E-9F9100FF0051' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '76054AB7-2ED2-4B36-91E4-9F9100FF31EA' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6E621002-C94E-4387-8404-9F9100FEE9C0' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B09F89D3-EB1E-43A2-975E-9F9100FF05AE' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F84934FE-FD08-4217-8838-9F9100FF0DA4' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '0500A741-ED5B-43BC-B7BC-9F9100FF2045' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'EC2A5ECB-8C9E-4AEC-82FD-9F96015A2A0D' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '9506C4E0-8621-4FBB-A7AF-9F96015AF38C' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='947645DA-5C04-4688-8BB0-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='57C7B1DC-E236-4E9A-8F79-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='39E2ED71-D8EF-4A1E-9F42-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C03E67DF-2684-4262-964D-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3B495160-D626-46E3-99B0-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D618DC71-49D1-4AC5-8EEC-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='72700132-4613-46E9-B48F-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='38E1E461-93AA-4194-AA55-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='15DB2A05-BCCD-41F5-930D-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='728A1AFB-D959-4487-8AD4-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F44D57DF-05ED-4C71-B13B-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9BD4CF5B-76A7-44FD-8591-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A4E66EF8-1191-45CC-B37C-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D8BD1F2-5F85-48CB-8871-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='12777718-F2A2-4E84-84C7-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BE91DD00-AC0A-42E9-A48A-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A4C1E491-7495-46E7-AD8E-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EADE2D12-D320-45FB-AF81-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C1304273-22FF-4924-9A23-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='639C9EBA-072B-44A2-8B11-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9EDEB557-6201-4005-AF17-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9C67BAD2-3839-4CE0-A145-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CAA3605C-6C10-436C-A0BA-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B3661514-E675-4D10-967E-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4716AB84-D7C1-40C9-A027-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DD42FF3A-F9CB-4999-B452-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BA67CD5F-A35F-41E0-B348-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EE837B26-9E64-4942-8974-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B9E14221-0F85-45AC-BD70-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D366C53-0C2C-4634-9104-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E7D308AE-65E3-4962-A532-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='24F799FD-5E15-4BDE-A60D-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C5137082-DF5F-4CB6-8D9F-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8F639A69-A99F-4E5B-AB1F-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D6F7DC11-4DC9-452E-9795-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1AB91834-DFC2-401A-851C-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C0E82DF0-76AD-49DE-B753-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='77F43F07-E8E0-4521-B155-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4A2CB1E4-201A-4985-AD5E-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BD21E245-2010-40B0-85F3-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='948B2B2E-34F3-4E38-B929-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E970F0C7-D6D1-4D92-A0CB-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D0838EB5-E812-45FE-B7EF-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='94B38371-97EF-4CF5-8F62-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='317D71CF-06F9-495E-B06E-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='56B147C6-A897-44BB-ABFB-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5DBE13F9-F515-4236-BF9D-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3A9B9CAF-627D-4B51-995D-A06000E4816C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E9B529A5-BD91-40A6-9253-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='1B70989B-1D54-408E-BDF7-9EE90119749F';

/** 713 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '422AE9F5-03DE-4AE2-830F-9EE901196F1C' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=713 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5DFE7500-2361-4EA9-BAEE-9F9100FF417D' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CC622E47-27BA-4409-98D3-9F9100FF4E71' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '686018FB-A520-47D2-A5E0-9F9100FF7190' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E7B1B9B2-4C5C-42A8-9995-9F9100FF553B' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4D142E58-52E8-4C2E-833E-9F9100FF83D8' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B4CC45E4-BA82-44F6-BB97-9F9100FF5C05' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'BFF044A2-D6ED-4FEE-921E-9F9100FF8BFD' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6D69C345-7FE5-4DC6-A301-9F9100FF46B8' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '22748379-83E5-4DC4-9A65-9F9100FF62BD' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'BE7D02D6-B95A-4FA1-8059-9F9100FF6AA4' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D455C95D-F6DB-4AF9-BF5A-9F9100FF7B22' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '0427F6EA-B3FA-4A6A-9B03-9F96015B3BC5' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'BDC2BA17-FC15-45CB-BEA4-9F96015B95ED' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='97F48E00-0F73-4A02-A47D-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='20E0DAB4-41DB-4B3F-8840-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9D610B1D-7C48-4708-AE14-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5A7DFE3B-1BCD-471F-8725-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='78E767C7-A9C2-4E88-A315-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F6B8CDBA-5D08-46F0-B072-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='382A642D-8CBF-4E7C-821F-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2193989D-D077-41A3-996D-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4FC39D74-C07B-4801-8152-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7C6DA54A-C0CA-47D8-8749-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9EC7093E-261A-4748-BA64-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='944F721B-76F9-4E0D-8564-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='96D6EFD8-27CA-4539-BEC9-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D39B588B-889D-490B-ACCC-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='14F80076-0F72-4B27-92F8-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1F11DB79-6DD8-4A76-851B-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='540E6D58-0248-4DF4-85E0-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='894FCD0C-E0B7-4A41-91D7-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9FD6A609-0B79-4471-8B74-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D37B73F1-FB78-4C11-B8E9-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BA8D8268-FA0F-451B-9E90-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3108FB27-8154-49F2-97BB-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B0B1AEAE-A78D-4CA0-B021-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6A85C234-0C09-4F1C-9BFA-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B81971F6-D0EC-4A44-9B1F-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6A21206C-1C21-43AC-91EA-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F453BFF1-CC02-4CA2-84C7-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0AF9DDF8-62AE-4C27-BDF4-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C00F2C5B-A2E9-4A69-B92A-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1969DC58-1EEF-42A5-8966-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CA691C7A-2272-47A0-9E46-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='25901056-5D75-44D0-AEBA-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BE86D862-03DD-4976-84B4-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2BF90D06-6CE5-4CF5-A0C4-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='09971BDC-7BC2-43B1-B4F7-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7EC2DB96-96C0-4F0F-BB80-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5A69D16B-1B36-4069-9A75-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='740FF264-BDF5-45B5-9A1A-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CF432761-98C1-4613-A6B2-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D9F321F6-C986-4C39-97FB-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0C880C5B-E476-4E68-AE34-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6E33B566-F6D4-46BB-8E0A-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8AAB44A2-27F9-46C7-97AC-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='57992C38-B237-45BC-9A0B-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9C25DE19-8B24-4C1F-A452-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B072D286-1B54-4889-AB24-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2BAE279E-EA81-4903-B6E3-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7104B495-1A24-42D9-9037-A06000E4816C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='03A0EF9E-6AA8-432A-8AD5-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='422AE9F5-03DE-4AE2-830F-9EE901196F1C';

/** 723 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '2F2BFF64-61AF-4CE3-8F74-9EE90119687C' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=723 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A1214F6A-99AC-41E8-9F8D-9F9100FF9E79' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EB35158B-3D87-444E-9440-9F9100FFA9D1' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '43051DB2-DBEE-415B-A585-9F9100FFCC47' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '54BC46D1-C140-4DC7-9BD8-9F9100FFB01D' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1BFDDB3A-728B-424B-A1A4-9F9100FFE072' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EB33D883-8E0D-4F35-8D15-9F9100FFB692' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '9C66E69E-0ABB-45F9-8DB6-9F9100FFE9A6' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2189B403-577A-4835-BA66-9F9100FFA3DF' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '9437872C-C874-4F0E-9424-9F9100FFBB92' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6BCFEE14-665B-4900-BE3F-9F9100FFC4E7' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '906A8E1C-BA36-496A-8EEC-9F9100FFD6FC' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F0E381BA-C39C-4F83-9414-9F96015BCDF8' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '3EC6B93E-98A4-41DE-9183-9F96015C17D9' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F22EA756-B0F6-4A19-9FC4-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='301368AF-F393-4B16-B681-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8E569D92-D517-4BFE-8578-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B6AB6D96-72FD-45F1-AB7A-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='51C55CB7-504B-48A8-8FAA-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='03496631-16E5-4973-A063-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A596DEBC-0A78-4E73-905A-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DCC94667-2976-4C82-ADE9-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8AB93033-0478-4219-8E99-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9CBAA9E1-FBE8-4458-80D8-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='67EF8142-86D9-4F36-B77E-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CC06FB2E-8F71-4FE9-8F41-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5699FB66-FE95-433F-832B-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='782106E0-2330-4C57-8F32-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='863DE5AF-3FD2-4070-B34E-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C7AB65A3-6E16-4817-9662-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='742C7662-489A-433E-909C-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A4440912-4CBA-4947-BE0A-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0472B09C-BAA2-417E-8C3E-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1AA0B19C-C000-4E14-9A25-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2F84B1FB-DF9D-409B-88E3-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F344EB49-318E-4888-B12A-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4A9A9B22-913E-4C73-8829-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='948AA531-0764-4F3C-A430-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B30B0C59-5D9B-4B1C-BE9B-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CC669E94-44F4-4520-A322-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2D612DA9-9861-4FF7-BBFC-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3D320297-AA0B-4D9E-8193-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='207769AB-A223-4D98-815B-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B0734B24-AAF2-4D82-BA3B-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FB8FBBBE-92FA-4EC6-8BEA-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='140D1986-CB33-418D-A271-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5F731A4B-5009-49D6-8161-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3E3365F1-59BA-4640-9174-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5C8D4FD4-CAD3-4D4D-88D0-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EB58DC6F-4E8C-4EC5-B06E-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='83602185-A389-4EDE-8846-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E3375FC6-594B-4B54-B0B7-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2ED32598-0C09-4B78-A229-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D40859E2-6DB4-4C0F-9E72-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6CB4E5CD-899A-45F4-9565-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AFFB918C-80D5-4F0E-848D-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ADFD629F-4700-4125-92DC-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0EDE9B8D-B55E-490E-A5C9-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A3F283CA-DEF6-4768-BBC0-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='748CC078-D76C-473D-A2E3-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A56F555D-45BD-41BF-A0A9-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7F69098A-2713-4E00-AF08-A06000E4816C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C654FF5F-3E5D-4363-B894-A05B0127B72C' and c.BPresent=1
and p.FKSubLine='2F2BFF64-61AF-4CE3-8F74-9EE90119687C';
