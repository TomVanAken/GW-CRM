
function isAuthor(dataSource) {
	importPackage(com.gw.application);
	try {
		return AppUtils.isAuthor(dataSource.getDocument())
	} catch(e) {
		xspOpenLog.logException(e, this);
	}
	return false;
}