const bg = document.querySelector(".background");

function openLogin() {
    show("login");
}

function openSignup() {
    show("signup");
}

function show(id) {
    bg.classList.add("blur");

    document.querySelectorAll(".content").forEach(c => {
        c.classList.add("hidden");
    });

    document.getElementById(id).classList.remove("hidden");
}
