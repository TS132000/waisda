﻿<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>

<div class="row equal-cols">

	<div class="admin-width-100pct">

		<section class="reset relative">
			<f:form commandName="form" class="admin-width-100pct" enctype="multipart/form-data" method="POST">
			    <f:errors cssClass="errors"/>
				<c:choose>
				    <c:when test="${form.importRunning == false || empty form.importRunning}">
				        <h1>Upload file</h1>
                        <fieldset class="admin-width-600px">
                            <div class="control-group">
                                <label for="file" cssClass="admin-label-200px">CSV File</label>
                                <div class="admin-value-300px">
                                    <input id="file" type="file" name="file" cssClass="admin-input-300px"/>
                                </div>
                            </div>
                        </fieldset>
                        <input type="submit" name="start" value="Start"/>
				    </c:when>
				    <c:otherwise>
                        <h1>Current stats</h1>
                        <fieldset class="admin-width-600px">
                            <script>
                                window.setTimeout(function() {
                                    //window.location.reload();
                                    $('#admin-body').load('./stats');
                                }, 4000);
                            </script>
                        </fieldset>
                        <input type="submit" name="stop" value="Stop"/>
                    </c:otherwise>
                </c:choose>
			</f:form>
		</section>
	</div>
</div>