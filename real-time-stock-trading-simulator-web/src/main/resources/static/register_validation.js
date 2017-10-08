$.validator.addMethod("passwordCheck",
    function (password) {
        if (!/[a-z]/.test(password)) {
            return false;
        } else if (!/[0-9]/.test(password)) {
            return false;
        }
        return true;
    },
    "Password must contain numbers and letters");

$.validator.addMethod("emailCheck",
    function (email) {
        let regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        return regex.test(email);
    },
    "Please enter a valid e-mail address");

$("#registerForm").validate({
    rules: {
        username: {
            required: true,
            minlength: 4,
            maxlength: 15,
        },
        password: {
            required: true,
            minlength: 8,
            passwordCheck: "#password",
        },
        password_confirmation: {
            equalTo: "#password"
        },
        email: {
            required: true,
            emailCheck: "#email"
        },
        email_confirmation: {
            equalTo: "#email"
        }
    },
    messages: {
        password_confirmation: "Passwords do not match",
    }
});