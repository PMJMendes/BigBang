update bigbang.tblBBCoverages
set CoverageName = UPPER(CoverageName);

update bigbang.tblDamageTypes
set DamageType = UPPER(DamageType);

update bigbang.tblInjuryCauses
set Cause = UPPER(Cause);

update bigbang.tblInjuryTypes
set InjuryType = UPPER(InjuryType);

update bigbang.tblInjuryParts
set BodyPart = UPPER(BodyPart);