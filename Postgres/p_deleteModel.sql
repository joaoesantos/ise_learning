------------------------------------
--          DELETE MODEL          --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_deletemodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;
	DELETE FROM USERS;
	DELETE FROM TAG;
	DELETE FROM RUNCODE;
	DELETE FROM CODELANGUAGE;
    COMMIT;
END;
$BODY$;
