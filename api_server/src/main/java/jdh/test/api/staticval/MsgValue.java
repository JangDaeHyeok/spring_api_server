package jdh.test.api.staticval;

/**
 * @분류 : 
 * @?��?��?���? : MsgValue
 * @?��?��?��?���? : 메시�? ?��?�� ?��?��?�� ?��?��?��
 */
public class MsgValue {

	public static final String SORRY              = "�?리자?�� 문의 ?��주세?��.";
	public static final String WRONG_ACCESS       = "?��못된 ?��근입?��?��.";
	public static final String SORRY_NO_PAGE      = "?���??��?�� ?��?���?�? 찾을?�� ?��?��?��?��.";
	public static final String SORRY_CALL_ADMIN   = "불편?�� ?��?�� ???��?�� 죄송?��?��?��. �?리자?�� 문의 ?��주세?��.";
	public static final String WAIT_TRY_AGAIN     = "불편?�� ?��?�� ???��?�� 죄송?��?��?��. ?��?�� ?�� ?��?�� ?��?��?��주세?��.";
	public static final String SORRY_NAME_DEFFI   = "본인 명의?�� ?��???�� 번호�? ?��증하?�� ?�� ?��?��?��?��.";
	public static final String SORRY_SEARCH_VALUE = "?��?��?�� 모두 ?��?�� ?�� �??�� ?��주세?��.";
	public static final String AUTH_NOT_FOUND	  = "권한?�� ?��?��?��?��.";
	
	public static final String SPACE = " ";
	public static final String ENTER = "\n";
	public static final String ENTER_ALERT = "\\n";
	public static final String TAB   = "\t";

	/****************************************************************************" CRUD */
	public static final String NOUN_INSERT = "추�?�?";
	public static final String NOUN_UPDATE = "?��?��?��";
	public static final String NOUN_SELECT = "�??��?��";
	public static final String NOUN_DELETE = "?��?���?";
	
	public static final String[] OBJECTS = {"?���?", "?��?��"};
	public static final String VERB = "?��???��?��?��.";
	public static final String COMPLETE = "?��료되?��?��?��?��.";

	/****************************************************************************" EXCEPTION */
	public static final String EXCEPTION = "문제�? 발생?��???��?��?��.";
	
	/****************************************************************************" LOGIN */
	public static final String LOGIN_COMMON_FAIL     = "로그?��?�� ?��?��?��???��?��?��." + ENTER_ALERT + "?��?�� ?��?��?�� 주세?��.";
	public static final String LOGIN_FIVE_TIMES_FAIL = "비�?번호 5?�� ?��?���? 로그?��?�� 불�??��?��?��." + ENTER_ALERT + "�?리자?���? 문의?��주세?��.";
	public static final String PW_PATTERN_FAIL = "비�?번호 ?��?��?�� ??립니?��."+ ENTER_ALERT +"?��?�� ?��?��?�� 주세?��.";
	
	/**=====================================[NAMO]
	 * @분류 : 
	 * @메소?���? : getDeleteMsg
	 * @?��?��?��?��?�� : 2020. 7. 23.
	 * @개발?�� : ?��강�??
	 * @리턴 : 
	 * @메소?��?���? : type = "기능�?" (?��:?��?��?��) / success = true / false
	 * ===========================================*/
	public static String getDeleteMsg(String type) {
		return type + SPACE + NOUN_DELETE + SPACE + OBJECTS[0] + SPACE + VERB;
	}
	public static String getDeleteMsg(String type, boolean success) {
		String successTxt = OBJECTS[0];
		if(!success) { successTxt = OBJECTS[1]; }
		return type + SPACE + NOUN_DELETE + SPACE + successTxt + SPACE + VERB;
	}
	
	/**=====================================[NAMO]
	 * @분류 : 
	 * @메소?���? : getSelectMsg
	 * @?��?��?��?��?�� : 2020. 7. 23.
	 * @개발?�� : ?��강�??
	 * @리턴 : 
	 * @메소?��?���? : type = "기능�?" (?��:?��?��?��) / success = true / false
	 * ===========================================*/
	public static String getSelectMsg(String type) {
		return type + SPACE + NOUN_SELECT + SPACE + OBJECTS[0] + SPACE + VERB;
	}
	public static String getSelectMsg(String type, boolean success) {
		String successTxt = OBJECTS[0];
		if(!success) { successTxt = OBJECTS[1]; }
		return type + SPACE + NOUN_SELECT + SPACE + successTxt + SPACE + VERB;
	}
	
	/**=====================================[NAMO]
	 * @분류 : 
	 * @메소?���? : getUpdateMsg
	 * @?��?��?��?��?�� : 2020. 7. 23.
	 * @개발?�� : ?��강�??
	 * @리턴 : type ?��?��?�� ?���? ?��???��?��?��.
	 * @메소?��?���? : type = "기능�?" (?��:?��?��?��) / success = true / false
	 * ===========================================*/
	public static String getUpdateMsg(String type) {
		return type + SPACE + NOUN_UPDATE + SPACE + OBJECTS[0] + SPACE + VERB;
	}
	public static String getUpdateMsg(String type, boolean success) {
		String successTxt = OBJECTS[0];
		if(!success) { successTxt = OBJECTS[1]; }
		return type + SPACE + NOUN_UPDATE + SPACE + successTxt + SPACE + VERB;
	}
	
	/**=====================================[NAMO]
	 * @분류 : 
	 * @메소?���? : getInsertMsg
	 * @?��?��?��?��?�� : 2020. 7. 23.
	 * @개발?�� : ?��강�??
	 * @리턴 : type ?��록이 ?���? ?��???��?��?��.
	 * @메소?��?���? : type = "기능�?" (?��:?��?��?��) / success = true / false
	 * ===========================================*/
	public static String getInsertMsg(String type) {
		return type + SPACE + NOUN_INSERT + SPACE + OBJECTS[0] + SPACE + VERB;
	}
	public static String getInsertMsg(String type, boolean success) {
		String successTxt = OBJECTS[0];
		if(!success) { successTxt = OBJECTS[1]; }
		return type + SPACE + NOUN_INSERT + SPACE + successTxt + SPACE + VERB;
	}
}
