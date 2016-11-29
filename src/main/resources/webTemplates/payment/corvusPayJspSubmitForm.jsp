<%--suppress JSUnresolvedFunction --%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>


<form:form name="corvusPayRedirectForm" action="${redirectUrl}" method="post" modelAttribute="requestData">

    <c:forEach items="${requestData.requestFields}" var="field">
        <input type="hidden" name="${field.key}" value="${field.value}">
    </c:forEach>

</form:form>

<script type="text/javascript">
    $(document).ready(function() {
        document.forms["corvusPayRedirectForm"].submit();
    });
</script>

