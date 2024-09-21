-- connects to the adempiere database and adempiere schema
\c adempiere adempiere

UPDATE AD_System SET IsFailOnMissingModelValidator = 'N';