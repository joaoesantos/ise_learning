------------------------------------
--          DELETE MODEL          --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_deletemodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;
	DELETE FROM app_user;
	DELETE FROM tag;
	DELETE FROM runcode;
	DELETE FROM code_language;
    COMMIT;
END;
$BODY$;
