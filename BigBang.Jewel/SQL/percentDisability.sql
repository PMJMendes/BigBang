-- alterar tabela
ALTER TABLE bbcomercial.tblMedicalDetails
ALTER COLUMN PercentDisability DECIMAL(9,5);

ALTER TABLE bbleiria.tblMedicalDetails
ALTER COLUMN PercentDisability DECIMAL(9,5);

ALTER TABLE credite_egs.tblMedicalDetails
ALTER COLUMN PercentDisability DECIMAL(9,5);

UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-11-27T11:55:55.763',[FKTypeDef]='66af024b-2e89-4e12-93f4-87e2e15120d3' WHERE [PK]='ef894062-34c9-46f4-9397-a13401167200';
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-11-27T11:55:58.217' WHERE [PK]='10480936-9597-4d94-b1dc-a1340114071d';

-- angola
ALTER TABLE bbangola.tblMedicalDetails
ALTER COLUMN PercentDisability DECIMAL(9,5);

UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-11-27T11:55:55.763',[FKTypeDef]='66af024b-2e89-4e12-93f4-87e2e15120d3' WHERE [PK]='ef894062-34c9-46f4-9397-a13401167200';
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-11-27T11:55:58.217' WHERE [PK]='10480936-9597-4d94-b1dc-a1340114071d';