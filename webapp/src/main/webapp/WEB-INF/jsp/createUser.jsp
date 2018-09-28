<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
<c:url value="/create" var="signUpUrl" />
<form:form modelAttribute="registerForm" action="${loginUrl}" method="post">
    <div>
        <form:label path="username">Username: </form:label>
        <form:input id="input2" path="username" type="text"/>
        <form:errors path="username" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="password">Password: </form:label>
        <form:input id="input3" path="password" type="password"/>
        <form:errors path="password" element="p"/>
    </div>
    <div>
        <form:label path="repeatPassword">Repeat Password: </form:label>
        <form:input id="input4" path="repeatPassword" type="password"/>
        <form:errors path="repeatPassword" element="p"/>
    </div>
    <div>
        <form:label path="firstName">First Name: </form:label>
        <form:input id="input5" path="firstName" type="text"/>
        <form:errors path="firstName" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="lastName">Last Name: </form:label>
        <form:input id="input6" path="lastName" type="text"/>
        <form:errors path="lastName" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="email">Email: </form:label>
        <form:input id="Input1" path="email" type="text"/>
        <form:errors path="email" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="cellphone">Cellphone: </form:label>
        <form:input id="input7" path="cellphone" type="text"/>
        <form:errors path="cellphone" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="cellphone">Birthday: </form:label>
            <div class="input-group date" id="timepicker-from" data-target-input="nearest">
                <input type="text" class="form-control datetimepicker-input" data-target="#timepicker-from"/>
                <div class="input-group-append" data-target="#timepicker-from" data-toggle="datetimepicker">
                    <div class="input-group-text"><i class="fas fa-clock"></i></div>
                </div>
            </div>
        <form:errors path="birthday" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="country">Country: </form:label>
        <form:input id="input8" path="country" type="text"/>
        <form:errors path="country" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="state">State: </form:label>
        <form:input id="input9" path="state" type="text"/>
        <form:errors path="state" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="city">City: </form:label>
        <form:input id="input10" path="city" type="text"/>
        <form:errors path="city" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="street">Street: </form:label>
        <form:input id="input11" path="street" type="text"/>
        <form:errors path="street" element="p" cssClass=""/>
    </div>
    <div>
        <input type="submit" value="Resgister!"/>
    </div>
</form:form>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js" type="text/javascript"></script>
<script  src="<c:url value="js/create-match.js"/>" type="text/javascript"></script>
</body>
</html>
