-- Changes in the notes columns' sizes for casualties and sub-casualties
ALTER TABLE credite_egs.tblBBCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbleiria.tblBBCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBCasualties ALTER COLUMN Description VARCHAR (4000);

ALTER TABLE credite_egs.tblBBCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbleiria.tblBBCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBCasualties ALTER COLUMN Notes VARCHAR (4000);

ALTER TABLE credite_egs.tblBBSubCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbleiria.tblBBSubCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBSubCasualties ALTER COLUMN Description VARCHAR (4000);

ALTER TABLE credite_egs.tblBBSubCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbleiria.tblBBSubCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBSubCasualties ALTER COLUMN Notes VARCHAR (4000);

