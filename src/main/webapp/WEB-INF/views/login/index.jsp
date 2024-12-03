<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>GNIS 3D MAP MANAGER</title>
    <link href="<c:url value='/resources/img/favicon.ico' />" type="image/x-icon" rel="shortcut icon" />
    <link href="<c:url value='/resources/css/common/reset.css' />" rel="stylesheet">
    <link href="<c:url value='/resources/css/common/index.css' />" rel="stylesheet">
    <link href="<c:url value='/resources/css/login/index.css' />" rel="stylesheet">
    <link href="<c:url value='/resources/css/modal/index.css' />" rel="stylesheet">
</head>

<body>
    <div class="container">
        <form id="loginFrm" name="loginFrm" class="login-form">
            <h2>
                <img src="<c:url value='/resources/img/logo.png' />" alt="경상남도">
            </h2>
            <p class="login-tit">
                <span class="txt">실내공간정보관리시스템</span>
            </p>
            <p class="login-txt">
                <span class="txt">LOGIN</span>
            </p>
            <div class="form-group login-input id">
                <label for="usrId">아이디</label>
                <input name="usrId" id="usrId" class="form-control" type="text" placeholder="아이디를 입력하세요.">
            </div>
            <div class="form-group login-input pw">
                <label for="usrPwd">패스워드</label>
                <input name="usrPwd" id="usrPwd" class="form-control" autocomplete="on" type="password" placeholder="패스워드를 입력하세요.">
            </div>
            <div class="btn-wrap">
                <button type="button" class="btn-w01 btn_remove">지우기</button>
                <button type="submit" class="btn-b01 btn-login">로그인</button>
            </div>
        </form>
    </div>
    <div class="modal-wrapper d-none">
        <div class="modal">
            <div class="icon-wrap">
                <span class="icon"></span>
            </div>
            <div class="modal-txt">
                <p id="errorMsg" class="txt1"></p>
                <p class="txt2">(관리자에게 문의 바랍니다.)</p>
            </div>
            <button class="btn_ok">
                <span class="txt">확인</span>
            </button>
        </div>
    </div>

<script src="<c:url value='/resources/js/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/resources/js/jquery/jquery.form.min.js' />"></script>
<script src="<c:url value='/resources/js/jquery/jquery-ui.min.js' />"></script>
<script src="<c:url value='/resources/js/jquery/jquery-ui-timepicker-addon.js' />"></script>
<script src="<c:url value='/resources/js/jquery/jquery.mtz.monthpicker.js' />"></script>
<script src="<c:url value='/resources/js/cmn/common.js' />"></script>
<script src="<c:url value='/resources/js/cmn/validation.js' />"></script>
<c:import url="/WEB-INF/views/cmn/constant.jsp" /> <!-- Constant -->
<script>
    const btnRemove = document.querySelector('BUTTON.btn_remove');
    btnRemove.addEventListener('pointerup', (evt) => {
        const inputUsrId = document.getElementById('usrId');
        const inputUsrPwd = document.getElementById('usrPwd');

        inputUsrId.value = '';
        inputUsrPwd.value = '';
    });

    const modalBtnOk = document.querySelector('.modal > BUTTON.btn_ok');
    modalBtnOk.addEventListener('pointerup', (evt)=> {
        document.querySelector('.modal-wrapper').classList.add('d-none');
    });

    $(function() {
        $("#loginFrm").ajaxForm({
            url: _CONTEXT_PATH_ + 'login/loginChk.json',
            type: 'post',
            dataType : 'json',
            beforeSubmit: function (data, frm, opt) {
                // 유효성 체크
                if(!jValidationFrm(frm)) return false;
            },
            success: function(res) {
                if(res.resultCd === 'fail') {
                    document.querySelector('.modal-wrapper').classList.remove('d-none');
                    switch (res.resultMsg) {
                        case 'WRONG_PWD' : {
                            document.getElementById('errorMsg').innerText = '아이디 또는 패스워드가 일치하지 않습니다.';
                            break;
                        }
                        case 'NONE_ACCESSIBLE_MAP' : {
                            document.getElementById('errorMsg').innerText = '접근 가능한 도면이 없습니다.';
                            break;
                        }
                        default : {
                            document.getElementById('errorMsg').innerText = '아이디 또는 패스워드가 일치하지 않습니다.';
                            break;
                        }

                    }
                    if(res.resultMsg === 'WRONG_PWD' ) {

                    } else i
                } else {
                    if(res.locationUrl) {
                        location.replace(res.locationUrl);
                    }
                }
            }
        });
    });
</script>
</body>
</html>