REM Script is used to initialize token with unlimited validity
REM Only to be used on local development environment; definitelly should not be used on production environment as it would present serious security breach
REM Designed to be run under KER, will give access to KER to any owner of created token

DECLARE
  l_Token ker_token_tb.tokenid%TYPE;
BEGIN
  l_Token:=KER_User_PG.mf_CreateToken;
  UPDATE
        ker_token_tb token
    SET
        tokenid='zZITYobxbDzZgVwpcnPnjGgujMDyQX'
      , expiration_date=KER_DtDate_EP.g_Max_DATE
    WHERE
          (tokenid=l_Token)
    ;
  KER_Server_EP.mp_Commit;
END;
/
