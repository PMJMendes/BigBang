/**  321 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'B3174C0F-DDF9-4466-A419-9EE90119ABDE' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=321 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C2F8E2E2-20A5-4B29-ACF7-9F91010152D0' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '48106845-7034-4EA2-BAB0-9F9101015950' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5BE422AE-163F-4F44-83DF-9F9101016007' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6D283F48-FEC1-44DD-9426-9F9101016880' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', 'N')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EA4D2E55-9F49-4654-BA4A-9FE201057D81' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', 'N')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from credite_egs.tblBBPolicies p
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(s.texto1, 'Desconhecida') ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
s.texto3 Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, s.texto2 SiteDescription
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, 'DE5D6DFE-B31A-4FBB-9681-9F96015E5AE9' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.TaxName like '%tipo%franq%' and c.BPresent=1
and p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, 'C2E79A22-5E7E-47A2-AC13-9FE201064022' FKField, NULL FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '6525BD79-5BF0-42D0-8719-9F96015E2699' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto8 Value, p.PK FKPolicy, '1842AED2-63A4-4B38-AC94-9FE2010680E2' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where t.TaxName like 'franq%' and c.BPresent=1
and p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where t.TaxName like 'capital' and c.BPresent=1
and p.FKSubLine='B3174C0F-DDF9-4466-A419-9EE90119ABDE';

/**  301 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '995699D1-1674-4B8E-B813-9EE90119A4F7' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=301 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C5666246-E2DE-4F2D-96B4-9F910101841F' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6B3382E2-C928-4011-99F2-9F9101018AB7' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2BF4EBE9-9E4D-491F-845E-9F91010192AA' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CCCB51E3-4BD3-4566-A12D-A05400C80DF9' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', 'N')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '73CAAA93-61D8-4BA2-AC45-9F9101019A01' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', 'N')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from credite_egs.tblBBPolicies p
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(s.texto1, 'Desconhecida') ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
s.texto3 Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, s.texto2 SiteDescription
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, '62CF16A7-DB7D-413F-9B90-9F96015F5E71' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.TaxName like '%tipo%franq%' and c.BPresent=1
and p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, 'C21E1D54-4FB0-4C82-A757-9FE20107AF0A' FKField, NULL FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '957BBB5C-F70B-4338-90A7-9F96015F291C' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'B2B2A3F5-7C52-4FAF-8128-9F96015F37E0' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '288422C6-F651-4AC7-AB91-9FE20107DA17' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto8 Value, p.PK FKPolicy, '814F6172-6C14-4CBA-AD6B-9FE20107FB20' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where t.TaxName like 'franq%' and c.BPresent=1
and p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '7344D9F9-25D3-49A9-B9A2-9F96015F4A65' FKField, o.PK FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where t.TaxName like 'capital' and c.BPresent=1
and p.FKSubLine='995699D1-1674-4B8E-B813-9EE90119A4F7';
