const CURRENT_LIBRARY_ID = Number(localStorage.getItem("LIBRARY_ID"));
fetch(`/api/profile/library/${CURRENT_LIBRARY_ID}`)
  .then(res => {
    if (!res.ok) throw new Error("Unauthorized");
    return res.json();
  })
  .then(data => {
    document.getElementById("adminName").innerText = data.adminName;
    console.log("admin name", data.adminName);
    document.getElementById("adminPhone").innerText = data.adminPhone;
    document.getElementById("libraryName").innerText = data.libraryName;
    document.getElementById("totalSeats").innerText = data.totalSeats;
  })
  .catch(() => {
    window.location.href = "/login.html";
  });

  function goTo(path) {
    window.location.href = path;
  }
