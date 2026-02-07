function signup() {

    const name = document.getElementById("name").value;
    const phone = document.getElementById("phone").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/signup", {
        method: "POST",
        credentials: "same-origin", // 🔥 IMPORTANT
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: name,
            phone: phone,
            password: password
        })
    })
    .then(res => {
        if (res.ok) {
            // 🔥 DIRECTLY GO TO CREATE LIBRARY
             window.location.href = "/createlibrary.html";
        } else {
            return res.text().then(msg => {
                alert("Signup failed: " + msg);
            });
        }
    });
}
