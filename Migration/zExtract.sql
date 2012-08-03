select 'insert into bigbang.tblLineCategories (PK, LineCatName) values (''' + CAST(PK AS VARCHAR(36)) + ''', N''' + LineCatName + ''');'
from bigbang.tblLineCategories
order by LineCatName;

select 'insert into bigbang.tblBBLines (PK, LineName, FKCategory) values (''' + CAST(l.PK AS VARCHAR(36)) + ''', N''' + l.LineName + ''', ''' + CAST(l.FKCategory AS VARCHAR(36)) + ''');'
from bigbang.tblBBLines l inner join bigbang.tblLineCategories k on k.PK=l.FKCategory
order by k.LineCatName, l.LineName;

select 'insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType, CalcClass, DefaultPercent, BIsLife, Description) values (''' +
CAST(s.PK AS VARCHAR(36)) + ''', N''' + s.SubLineName + ''', ''' + CAST(s.FKLine AS VARCHAR(36)) + ''', ''' + CAST(s.FKObjectType AS VARCHAR(36)) + ''', ''' +
CAST(s.FKPeriodType AS VARCHAR(36)) + ''', ' + ISNULL('N''' + s.CalcClass + '''', 'NULL') + ', ' + ISNULL(CAST(s.DefaultPercent AS VARCHAR), 'NULL') + ', ' +
CAST(s.BIsLife AS VARCHAR(1)) + ', ' + ISNULL('N''' + s.Description + '''', 'NULL') + ');'
from bigbang.tblBBSubLines s inner join bigbang.tblBBLines l on l.PK=s.FKLine inner join bigbang.tblLineCategories k on k.PK=l.FKCategory
order by k.LineCatName, l.LineName, s.SubLineName;

select 'insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader, Tag, COrder) values (''' + CAST(c.PK AS VARCHAR(36)) + ''', N''' +
c.CoverageName + ''', ''' + CAST(c.FKSubLine AS VARCHAR(36)) + ''', ' + CAST(c.BMandatory AS VARCHAR(1)) + ', ' + CAST(c.BHeader AS VARCHAR(1)) + ', ' +
ISNULL('N''' +c.Tag + '''', 'NULL') + ', ' + ISNULL(CAST(c.COrder AS VARCHAR), 'NULL') + ');'
from bigbang.tblBBCoverages c inner join bigbang.tblBBSubLines s on s.PK=c.FKSubLine inner join bigbang.tblBBLines l on l.PK=s.FKLine
inner join bigbang.tblLineCategories k on k.PK=l.FKCategory
order by k.LineCatName, l.LineName, s.SubLineName, c.BHeader desc, c.BMandatory desc, c.CoverageName;

select 'insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory, Tag, BVisible) values (''' +
CAST(t.PK AS VARCHAR(36)) + ''', N''' + t.TaxName + ''', ''' + CAST(t.FKCoverage AS VARCHAR(36)) + ''', ''' + CAST(t.FKFieldType AS VARCHAR(36)) + ''', ' + ISNULL('N''' + t.Units + '''', 'NULL') + ', ' +
ISNULL('N''' + t.DefaultValue + '''', 'NULL') + ', ' + CAST(t.BVariesByObject AS VARCHAR(1)) + ', ' + CAST(t.BVariesByExercise AS VARCHAR(1)) + ', ' +
ISNULL('''' + CAST(t.FKReferenceTo AS VARCHAR(36)) + '''', 'NULL') + ', ' + CAST(t.ColumnOrder AS VARCHAR(3)) + ', ' + CAST(t.BMandatory AS VARCHAR(1)) + ', ' + ISNULL('N''' + t.Tag + '''', 'NULL') + ', ' +
CAST(t.BVisible AS VARCHAR(1)) + ');'
from bigbang.tblBBTaxes t inner join bigbang.tblBBCoverages c on c.PK=t.FKCoverage inner join bigbang.tblBBSubLines s on s.PK=c.FKSubLine inner join bigbang.tblBBLines l on l.PK=s.FKLine
inner join bigbang.tblLineCategories k on k.PK=l.FKCategory
order by k.LineCatName, l.LineName, s.SubLineName, c.BHeader desc, c.BMandatory desc, c.CoverageName, t.BVisible desc, t.BMandatory desc, t.BVariesByObject, t.BVariesByExercise, t.TaxName;
