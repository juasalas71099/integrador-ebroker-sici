package com.cmscomunidades.CMS.base.jpa;

public enum MatchMode {
	STARTS_WITH, ENDS_WITH, EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS, GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN,
	LESS_THAN_EQUAL, BEFORE, AFTER, IN, NOT_IN;
	
	public static String CONTAINS_PATTERN = "%.*%";
	public static String NOT_CONTAINS_PATTERN = "~%.*%";

	public static MatchMode fromString(String value) {
		MatchMode operation = MatchMode.EQUALS;
		if (value.startsWith("!")) {
			operation = MatchMode.NOT_EQUALS;
		}
		if (value.startsWith(">")) {
			operation = MatchMode.GREATER_THAN;
		}
		if (value.startsWith(">=")) {
			operation = MatchMode.GREATER_THAN_EQUAL;
		}
		if (value.startsWith("<")) {
			operation = MatchMode.LESS_THAN;
		}
		if (value.startsWith("<=")) {
			operation = MatchMode.LESS_THAN_EQUAL;
		}
		if (value.matches(CONTAINS_PATTERN)) {
			operation = MatchMode.CONTAINS;
		}
		if (value.matches(NOT_CONTAINS_PATTERN)) {
			operation = MatchMode.NOT_CONTAINS;
		}
		return operation;
	}

	public static String clearValue(String value) {
		value = value.replace("!", "");
		value = value.replace(">=", "");
		value = value.replace("<=", "");
		value = value.replace(">", "");
		value = value.replace("<", "");
		value = value.replace("%", "");
		value = value.replace("~", "");

		//value = value.replace(">>", "");
		//value = value.replace("<<", "");
		return value;
	}

}

//"startsWith": "Start with",
//"contains": "Contains",
//"notContains": "Not contains",
//"endsWith": "Ends with",
//"equals": "Equals",
//"notEquals": "Not equals",
//"noFilter": "No Filter",
//"lt": "Less than",
//"lte": "Less than or equal to",
//"gt": "Greater than",
//"gte": "Greater than or equal to",
//"is": "Is",
//"isNot": "Is not",
//"dateIs": "Date is",
//"dateIsNot": "Date is not",
//"dateAfter": "Date is after",
//"dateBefore": "Date is before",
//"before": "Before",
//"after": "After",