/** 708 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'DB605A61-8AC7-427A-8A1C-9F340151C1A9' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=708 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6BAF112B-C5E9-4FF0-A0C7-9F9100FBC050' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5091EE55-C18C-4F43-B402-9F9100FBCF5B' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '486CC859-D2A1-4C70-88E8-9F9100FBFDE4' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '25209EFA-0A9B-4127-BB67-9F9100FBD701' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '82C211F8-FDA5-4D7F-A580-9F9100FC104E' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '97508E04-2328-4AE2-A848-9F9100FBE230' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3043ECBD-DAF2-4933-B227-9F9100FC1836' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2A884F86-5752-4367-9091-9F9100FBC6C5' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '386596D0-0C43-4181-ACF7-9F9100FBE9A7' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2EE268DD-F91E-4C1C-AD99-9F9100FBF54B' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EB60AC74-F6C4-4615-880A-9F9100FC07B4' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBSubPolicies (PK, SubPolicyNumber, FKProcess, FKClient, BeginDate, EndDate, FKFractioning, SubPolicyNotes, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ss.apolice SubPolicyNumber, null FKProcess, c.PK FKClient, ss.datini BeginDate, ss.datfim EndDate, f.PK FKFractioning, substring(ss.observ, 1, 250) SubPolicyNotes,
case ss.situacao when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(ss.moeda, 1) when 2 then ss.vpremio else ss.vpremio/200.482 end, 2) Premium, ss.DocuShare DShareFolder, ss.MigrationID MigrationID
from credegs..empresa.apolice ss
inner join credite_egs.tblbbclients c on c.MigrationID=ss.cliente
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = ss.fpagamento COLLATE DATABASE_DEFAULT
where ss.ramo=10708 and ss.texto2=708 and (ss.situacao in ('P', 'N') or (ss.situacao in ('A', 'U') and (ss.datfim is null or ss.datfim>'2009-12-31')));

insert into credite_egs.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
b.PK FKSubPolicy, z.FKCoverage, z.BPresent
from credite_egs.tblBBSubPolicies b
inner join credegs..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join credegs..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages z on z.FKPolicy=p.PK
where s.ramo=708 and ss.ramo=10708;

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, '2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' FKTaxAsSubList
from (select distinct case ltrim(rtrim(isnull(texto2, ''))) when '' then null else
case left(texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end v
from credegs..empresa.objectos where ramo=10708) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F');

insert into credite_egs.tblSubPolicyObjects (PK, ObjName, FKSubPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex,
DateOfBirth, ClientNumberI, InsurerIDI, MigrationAux)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
o.Texto1 ObjName, b.PK FKSubPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType, c.Address1 Address1, c.Address2 Address2, c.FKZipCode FKZipCode,
o.ValidoDesde InclusionDate, o.ValidoAte ExclusionDate, case when left(isnull(o.Texto2, ''), 1)='T' then c.FiscalNumber else NULL end FiscalNumberI, s.PK FKSex, NULL DateOfBirth,
case when left(isnull(o.Texto2, ''), 1)='T' then c.ClientNumber else NULL end ClientNumberI, NULL InsurerIDI, o.NumObjecto MigrationAux
from credegs..empresa.objectos o
inner join credegs..empresa.apolice ss on ss.cliente=o.cliente and ss.apolice=o.apolice and ss.ramo=o.ramo and ss.comseg=o.comseg
inner join credite_egs.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join credite_egs.tblbbclients c on c.PK=b.FKClient
left outer join bigbang.tblSex s on left(s.SexName, 1) COLLATE DATABASE_DEFAULT =left(o.Texto3, 1) COLLATE DATABASE_DEFAULT
where o.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, ss.PK FKSubPolicy, '2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.NumObjecto=z.MigrationAux
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case ltrim(rtrim(isnull(o.texto2, ''))) when '' then null else
case left(o.texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when o.texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F')
and s.ramo=10708 and s.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, 'EBBFEF55-3334-4A99-AE4E-9FE200CA7D72' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '8C6A4578-57AB-45FB-B6B2-A05B0107E6CD' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '6958DBA5-7A21-48F7-BE75-A05B0107D24D' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'CD45F751-7A25-4BA9-99B4-9FE200CAAD4B' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Titular'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '030B85CD-71C5-4891-9113-9FE200CACE3C' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Cônjuge'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '48BD5524-3DB0-4F82-8BEA-9FE200CD6620' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Maior'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '17B5E1DA-D77A-43A5-AB12-9FE200CD8792' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Menor'
and s.ramo=10708 and s.texto2=708
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'D56812DE-E5F5-4C20-B4B5-9F9601575FB8' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Titular'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '20CD0AC8-9FFA-4928-852B-9F9601577C06' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Cônjuge'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'CF516E0B-C36F-476C-BFCC-9FE200CA297D' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Maior'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '591DCF99-34E8-4211-A17C-9F960157A4F7' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F' and x.ItemValue='Filho Menor'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=708
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=708;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, 'F686E0E4-1517-41BB-97D7-9FE200CDB525' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblBBSubPolicies ss
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=708;

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theCount Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, sum(cast(v.Value AS INTEGER)) theCount
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credegs..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join credite_egs.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join credite_egs.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('CD45F751-7A25-4BA9-99B4-9FE200CAAD4B', '030B85CD-71C5-4891-9113-9FE200CACE3C', '48BD5524-3DB0-4F82-8BEA-9FE200CD6620', '17B5E1DA-D77A-43A5-AB12-9FE200CD8792')
and ss.ramo=10708 and s.ramo=708
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('CD45F751-7A25-4BA9-99B4-9FE200CAAD4B', '030B85CD-71C5-4891-9113-9FE200CACE3C', '48BD5524-3DB0-4F82-8BEA-9FE200CD6620', '17B5E1DA-D77A-43A5-AB12-9FE200CD8792')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, max(v.Value) theMax
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credegs..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join credite_egs.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join credite_egs.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('D56812DE-E5F5-4C20-B4B5-9F9601575FB8', '20CD0AC8-9FFA-4928-852B-9F9601577C06', 'CF516E0B-C36F-476C-BFCC-9FE200CA297D', '591DCF99-34E8-4211-A17C-9F960157A4F7')
and ss.ramo=10708 and s.ramo=708
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('D56812DE-E5F5-4C20-B4B5-9F9601575FB8', '20CD0AC8-9FFA-4928-852B-9F9601577C06', 'CF516E0B-C36F-476C-BFCC-9FE200CA297D', '591DCF99-34E8-4211-A17C-9F960157A4F7')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F686E0E4-1517-41BB-97D7-9FE200CDB525' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5C5EDCE3-60BB-4671-B46F-A05B0103E044' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='610ACE63-2D2B-450F-8F8F-A05B01042690' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='28905900-639E-48B6-8A80-A05B01049ADB' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0364A72E-6D63-483A-A45D-A05B0104BAFA' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D06601AC-3C1D-49BD-82C7-A05B0104E054' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5F442716-CCFF-404F-817A-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='361B6E77-8AB4-424B-AF8E-A05B01040339' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='585629AC-D46B-4AB1-BECB-A05B01046B65' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='93A105C5-934D-45C1-9D3B-A05B01051873' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FDDFE8E8-28DD-4765-A567-A05B010564BF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='115A69CF-1822-4380-AF37-A05B0105B724' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CE76D738-2AFC-461C-8E87-A05B010593BF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D89206E2-458E-4340-8938-A05B0105FE69' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D7F26719-6401-44F5-B076-A05B0105D434' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='15E73725-E734-457D-AE6F-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CE2F7E84-0DCD-431B-8E73-A05B01053785' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C739EA9E-DCF3-4C33-B866-A05B01062933' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='709FDB03-2DDB-415F-8AFF-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E4A1F7E8-C781-46C7-A8B1-A05B010651AF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A91BA07B-10E1-4B36-BD5C-A05B0106EDB6' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9470215D-A199-4BCA-B883-A05B01077D3C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C42AEDD3-1DA6-464A-B47C-A05B0107A6D1' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E9A5AC18-EFE5-4024-B836-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EEABAA50-AFD7-4EB0-BF56-A05B01066F47' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C46854F3-B0F5-4049-949F-A05B01085305' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='164EA58C-F708-4D5A-AA00-A05B0108A5DB' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='38695493-C873-4427-8D67-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6ECCF2BF-BE9E-4894-80EA-A05B010888AF' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='959A17D9-7A12-43D7-8FB3-A05B0108E3E7' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EFB8B428-6EE1-4E2D-9FA9-A05B010919BB' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1A82F860-58FB-4F18-A376-A05B010988B4' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D558BA3-DEF0-4B11-9080-A05B0109A721' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='376A72D0-184B-4D48-BA26-A05B0109F3C3' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='63B50259-BA53-4870-9B95-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F6A8668C-FE8A-4D6F-9A59-A05B0108FD9E' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='37489DD2-CA1C-4E1B-8E40-A05B01095A1A' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D0578CA-97ED-4AB6-A463-A05B010B3969' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2B2B82A6-BE5A-48A9-BE2F-A05B010B7482' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='49995F58-FF82-4035-90DB-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F2DDA4E3-56B6-45C8-868F-A05B010B58F6' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='57A71B80-9D24-4CF1-ACED-A05B010BC952' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BAFEA98D-C217-44D7-9822-A05B010C0DD3' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D669D1B9-0BF4-4CE5-95C2-A05B010C3C55' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2CDDE0B2-8695-41EC-AB91-A05B010C5C11' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5C914CEA-A8E3-4508-B3FE-A05B010C7C42' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EE6E630C-8B0C-426F-A280-A06000E4816C' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E4AC0A85-D6DA-46C4-B102-A05B010BE743' and c.BPresent=1
and p.FKSubLine='DB605A61-8AC7-427A-8A1C-9F340151C1A9';

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.Value Value, b.PK FKSubPolicy, v.FKField FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblBBSubPolicies b
inner join credegs..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join credegs..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyValues v on v.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.PK=v.FKField
inner join bigbang.tblBBCoverages c on c.PK=t.FKCoverage
where c.BHeader=0
and s.ramo=708 and ss.ramo=10708;

/** 718 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=718 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '45823667-05AC-4F75-8F73-9F9100FC2F2E' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F36EF742-51E3-494D-8AA6-9F9100FC3C1E' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '212FBAE7-B7DD-412F-90A0-9F9100FC68DC' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F607DB83-88D8-4997-8130-9F9100FC43BF' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco4, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6345B6E1-E79B-45CF-80CA-9F9100FC7E66' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1F0977A5-C0EF-477F-9210-9F9100FC4AD0' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco5, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'AB99B436-91CB-4DC3-B0C7-9F9100FC8637' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '746EE69B-F8EA-4F02-B566-9F9100FC34DF' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco6, '')+isnull(s.risco7, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'AD416A46-6C74-4A54-9FBB-9F9100FC5214' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A8158091-7DFE-4E89-A3B4-9F9100FC5B6E' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '902909C4-985D-491F-BA0A-9F9100FC760E' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBSubPolicies (PK, SubPolicyNumber, FKProcess, FKClient, BeginDate, EndDate, FKFractioning, SubPolicyNotes, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ss.apolice SubPolicyNumber, null FKProcess, c.PK FKClient, ss.datini BeginDate, ss.datfim EndDate, f.PK FKFractioning, substring(ss.observ, 1, 250) SubPolicyNotes,
case ss.situacao when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(ss.moeda, 1) when 2 then ss.vpremio else ss.vpremio/200.482 end, 2) Premium, ss.DocuShare DShareFolder, ss.MigrationID MigrationID
from credegs..empresa.apolice ss
inner join credite_egs.tblbbclients c on c.MigrationID=ss.cliente
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = ss.fpagamento COLLATE DATABASE_DEFAULT
where ss.ramo=10708 and ss.texto2=718 and (ss.situacao in ('P', 'N') or (ss.situacao in ('A', 'U') and (ss.datfim is null or ss.datfim>'2009-12-31')));

insert into credite_egs.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
b.PK FKSubPolicy, z.FKCoverage, z.BPresent
from credite_egs.tblBBSubPolicies b
inner join credegs..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join credegs..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages z on z.FKPolicy=p.PK
where s.ramo=718 and ss.ramo=10708;

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, 'AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' FKTaxAsSubList
from (select distinct case ltrim(rtrim(isnull(texto2, ''))) when '' then null else
case left(texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end v
from credegs..empresa.objectos where ramo=10708) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF');

insert into credite_egs.tblSubPolicyObjects (PK, ObjName, FKSubPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex,
DateOfBirth, ClientNumberI, InsurerIDI, MigrationAux)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
o.Texto1 ObjName, b.PK FKSubPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType, c.Address1 Address1, c.Address2 Address2, c.FKZipCode FKZipCode,
o.ValidoDesde InclusionDate, o.ValidoAte ExclusionDate, case when left(isnull(o.Texto2, ''), 1)='T' then c.FiscalNumber else NULL end FiscalNumberI, s.PK FKSex, NULL DateOfBirth,
case when left(isnull(o.Texto2, ''), 1)='T' then c.ClientNumber else NULL end ClientNumberI, NULL InsurerIDI, o.NumObjecto MigrationAux
from credegs..empresa.objectos o
inner join credegs..empresa.apolice ss on ss.cliente=o.cliente and ss.apolice=o.apolice and ss.ramo=o.ramo and ss.comseg=o.comseg
inner join credite_egs.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join credite_egs.tblbbclients c on c.PK=b.FKClient
left outer join bigbang.tblSex s on left(s.SexName, 1) COLLATE DATABASE_DEFAULT =left(o.Texto3, 1) COLLATE DATABASE_DEFAULT
where o.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, ss.PK FKSubPolicy, 'AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.NumObjecto=z.MigrationAux
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case ltrim(rtrim(isnull(o.texto2, ''))) when '' then null else
case left(o.texto2, 1) when 'T' then 'Titular' when 'C' then 'Cônjuge' else
case when o.texto2 in ('Filha', 'Filho') then 'Filho Menor' else 'Filho Maior' end end end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='2A4C2D81-ED02-48AD-B6DD-9FE200CA630F')
and s.ramo=10708 and s.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '8118E542-ECB6-4FB0-BC0C-9FE200EC19FF' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '6A9B4E45-BE0B-4D39-BBF1-A05B01240F6E' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '563187CF-0064-4449-AB75-A05B01240F6E' FKField, z.PK FKObject, NULL FKExercise
from credite_egs.tblSubPolicyObjects z
inner join credite_egs.tblBBSubPolicies ss on ss.PK=z.FKSubPolicy
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '7B3DF1B7-5162-4DF1-BDCE-9FE200EDF0B0' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Titular'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, '17798432-8AF2-4135-B792-9FE200F23697' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Cônjuge'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'F3C6A12C-1EDC-4D73-BF1D-9FE200F25296' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Maior'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(zz.theCount, 0) Value, s.PK FKSubPolicy, 'B38F365D-0828-4E56-9471-9FE200F5443B' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, count(z.PK) theCount
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Menor'
and s.ramo=10708 and s.texto2=718
group by ss.PK, x.ItemValue) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '6CCA6707-6835-48F2-B000-9F960157C8AF' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Titular'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '88F74A16-1A9C-4B63-B635-9F960157E5FD' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Cônjuge'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, '35A7C9CA-E949-4297-B5AB-9FE200EA2C8A' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Maior'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, s.PK FKSubPolicy, 'C1C6B0C1-4120-41D5-8F32-9F960157FC5B' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbsubpolicies s
left outer join (select ss.PK FKSubPolicy, max(o.Texto5) theMax
from credite_egs.tblBBSubPolicies ss
inner join credite_egs.tblSubPolicyObjects z on z.FKSubPolicy=ss.PK
inner join credite_egs.tblBBSubPolicyValues v on v.FKObject=z.PK
inner join credite_egs.tblPolicyValueItems x on x.PK=v.Value
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
inner join credegs..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo and o.numobjecto=z.MigrationAux
where v.FKField='AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF' and x.ItemValue='Filho Menor'
and o.Texto5 is not null
and s.ramo=10708 and s.texto2=718
group by ss.PK) zz on zz.FKSubPolicy=s.PK
inner join credegs..empresa.apolice ss on ss.MigrationID=s.MigrationID
where ss.ramo=10708 and ss.texto2=718;

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, ss.PK FKSubPolicy, '5ECAC9CB-5A5D-4651-B9A1-9FE200F578C8' FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblBBSubPolicies ss
inner join credegs..empresa.apolice s on s.MigrationID=ss.MigrationID
where s.ramo=10708 and s.texto2=718;

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theCount Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, sum(cast(v.Value AS INTEGER)) theCount
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credegs..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join credite_egs.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join credite_egs.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('7B3DF1B7-5162-4DF1-BDCE-9FE200EDF0B0', '17798432-8AF2-4135-B792-9FE200F23697', 'F3C6A12C-1EDC-4D73-BF1D-9FE200F25296', 'B38F365D-0828-4E56-9471-9FE200F5443B')
and ss.ramo=10708 and s.ramo=718
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('7B3DF1B7-5162-4DF1-BDCE-9FE200EDF0B0', '17798432-8AF2-4135-B792-9FE200F23697', 'F3C6A12C-1EDC-4D73-BF1D-9FE200F25296', 'B38F365D-0828-4E56-9471-9FE200F5443B')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
zz.theMax Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblbbpolicies p
left outer join (select p.PK FKPolicy, v.FKField FKField, max(v.Value) theMax
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credegs..empresa.apolice ss on ss.texto4=s.cliente and ss.texto1=s.apolice and ss.texto2=s.ramo and ss.texto3=s.comseg
inner join credite_egs.tblbbsubpolicies b on b.MigrationID=ss.MigrationID
inner join credite_egs.tblbbsubpolicyvalues v on v.FKSubPolicy=b.PK
where v.FKField in ('6CCA6707-6835-48F2-B000-9F960157C8AF', '88F74A16-1A9C-4B63-B635-9F960157E5FD', '35A7C9CA-E949-4297-B5AB-9FE200EA2C8A', 'C1C6B0C1-4120-41D5-8F32-9F960157FC5B')
and ss.ramo=10708 and s.ramo=718
group by p.PK, v.FKField) zz on zz.FKPolicy=p.PK,
bigbang.tblBBTaxes t
where t.PK in ('6CCA6707-6835-48F2-B000-9F960157C8AF', '88F74A16-1A9C-4B63-B635-9F960157E5FD', '35A7C9CA-E949-4297-B5AB-9FE200EA2C8A', 'C1C6B0C1-4120-41D5-8F32-9F960157FC5B')
and (t.PK=zz.FKField or zz.FKField is NULL)
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '5ECAC9CB-5A5D-4651-B9A1-9FE200F578C8' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C2320EA7-837E-4948-84C7-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FDFE5828-6A8B-411A-9B09-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='03459036-7A3F-4293-A63D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5D022BEB-D96E-42D3-A867-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ACA75A0A-0099-4AA4-83A1-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C0DBB220-EF4F-46B1-BB40-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7E129ACA-B333-4F61-A8B0-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B7989DE6-3664-4295-A44D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8B2C4A1A-242B-40BA-B868-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D5182C54-44EA-43E2-99AF-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='33010D4F-BF1A-4DF8-B010-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5AC5E573-F1EF-418E-9AD4-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1950FB13-43EF-46ED-9FB7-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FA044DDA-5544-4E96-A855-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D75E5DDF-AC6F-4B08-87A8-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EE51B266-5C8E-490D-B87D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B44AE800-74C2-4433-B668-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0C03DEC3-0076-4B52-91FE-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='286C8180-4313-467C-ABDB-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E0EDEF7B-E98E-4442-AD7E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A0EE188D-1ECD-421C-BD15-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D8DA5B8D-4C25-4A99-9673-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='29BF3B05-D7CA-4BB7-B6A7-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A42759DC-87ED-4CCF-AA87-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='52A8B818-5227-4DDC-B18B-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D4AF214B-8F2A-4511-A614-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B6282A6A-A17C-46A9-BB1E-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FABEAB38-D2A2-4BC0-AB12-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'0' Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BC3A2B43-8AE9-4292-9844-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0300F72E-55ED-404E-BF05-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6213502B-B5C0-4422-9C6D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F749AEB5-B4B8-45EF-A91A-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2C8F5E4E-6167-401D-B66D-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DC4F8EB2-8F7B-46E8-9708-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C7BA2642-79F1-4F98-8866-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2A30A0F9-CD22-4CA4-AA64-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0CD192C5-FED6-4032-8CF8-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8875CCBC-A7B8-4542-9D23-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9BED185F-04D1-4E68-8966-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7C9F7246-055B-438A-A32A-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='87374767-7952-4900-81B5-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ABCA35D7-86C9-4644-BC03-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CCCF397D-626E-4D83-8FB8-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='55788785-726F-43C8-834E-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='99FC80B4-CA85-49B4-9309-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='246BD4C1-BB03-4968-A539-A06000E4816C' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E24C7ACC-5FB9-4713-BA35-A05B01240F6E' and c.BPresent=1
and p.FKSubLine='E2E6041E-1C8A-41F4-B6B9-9F340151BAA1';

insert into credite_egs.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.Value Value, b.PK FKSubPolicy, v.FKField FKField, NULL FKObject, NULL FKExercise
from credite_egs.tblBBSubPolicies b
inner join credegs..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join credegs..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyValues v on v.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.PK=v.FKField
inner join bigbang.tblBBCoverages c on c.PK=t.FKCoverage
where c.BHeader=0
and s.ramo=718 and ss.ramo=10708;
