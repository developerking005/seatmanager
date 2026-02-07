fetch("/api/profile")
  .then(res => {
    if (!res.ok) throw new Error("Unauthorized");
    return res.json();
  })
  .then(data => {
    document.getElementById("adminName").innerText = data.adminName;
    document.getElementById("adminPhone").innerText = data.adminPhone;
    document.getElementById("libraryName").innerText = data.libraryName;
    document.getElementById("totalSeats").innerText = data.totalSeats;
  })
  .catch(() => {
    window.location.href = "/login.html";
  });
