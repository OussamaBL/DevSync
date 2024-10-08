<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.css"  rel="stylesheet" />

</head>
<body>

<div class="flex justify-center items-center min-h-screen bg-gray-100">
    <form class="max-w-sm w-full bg-white p-6 rounded-lg shadow-md" action="login" method="POST">
        <h1 class="text-2xl text-center font-semibold text-indigo-800 mb-6">Login</h1>
        <div class="mb-5">
            <label for="email" class="block mb-2 text-sm font-medium text-gray-900">Your email</label>
            <input type="email" id="email" name="email" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" placeholder="name@flowbite.com" />
            <input type="hidden"  name="_method" value="post" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" placeholder="name@flowbite.com" />
            <% String msg = (String) request.getAttribute("error"); %>
            <span class="text-red-500 text-sm">
          <%= (msg != null) ? msg : "" %>
    </span>
        </div>
        <div class="mb-5">
            <label for="password" class="block mb-2 text-sm font-medium text-gray-900">Your password</label>
            <input type="password" id="password" name="password" placeholder="***********" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" />
        </div>
        <button type="submit" class="text-white w-full bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">Submit</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
<script>
    const form = document.querySelector('form');


    form.addEventListener('submit', function (event) {


        let isValid = true;

        const email = document.getElementById('email');
        const password = document.getElementById('password');
        console.log(email,password,form)


        clearErrors();

        if (password.value.trim() === '' || password.value.trim().length < 7) {
            showError(password, 'Name of tag  must be at least 8 characters.');
            isValid = false;
        }

        const emailPattern = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
        if (!emailPattern.test(email.value.trim())) {
            showError(email, 'Please enter a valid email address.');
            isValid = false;
        }




        if (!isValid) {
            event.preventDefault();
        }
    });


    function showError(element, message) {
        const error = document.createElement('span');
        error.className = 'text-red-600 text-sm';
        error.innerText = message;
        element.parentElement.appendChild(error);
        element.classList.add('border-red-500');
    }


    function clearErrors() {
        const errors = document.querySelectorAll('.text-red-600');
        errors.forEach(error => error.remove());
        const inputs = document.querySelectorAll('.border-red-500');
        inputs.forEach(input => input.classList.remove('border-red-500'));
    }

</script>
</body>

</html>
