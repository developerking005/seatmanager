fetch("/api/student")
  .then(res => {
    if (!res.ok) throw new Error("Unauthorized");
    return res.json();
  })
  .then(data => {
    const table = document.getElementById("studentsTable");
    table.innerHTML = "";

//    data.forEach(s => {
//      const row = document.createElement("tr");
//
//      row.innerHTML = `
//        <td>${s.seatNumber}</td>
//        <td>${s.name}</td>
//        <td>${s.phone}</td>
//        <td>${formatDate(s.startDate)}</td>
//        <td>${formatDate(s.endDate)}</td>
//        <td>₹${s.amount}</td>
//        <td>
//          <button onclick="${s.id}">Vacate</button>
//        </td>
//      `;
//      row.onclick = () => vacate(s.id);
//
//      table.appendChild(row);
//    });

    data.forEach(s => {
      const row = document.createElement("tr");
      row.classList.add("student-row");
       row.innerHTML = `
          <td>${s.seatNumber}</td>
          <td>${s.name}</td>
          <td>${s.phone}</td>
          <td>${formatDate(s.endDate)}</td>
          <td>₹${s.amount}</td>
          <td>
            <button onclick="vacate(${s.id}); event.stopPropagation()">Vacate</button>
          </td>
        `;
      // 🔥 THIS IS IMPORTANT
      row.addEventListener("click", () => {
        document.querySelectorAll(".table tr").forEach(r => r.classList.remove("active"));
        row.classList.add("active");
        loadStudentProfile(s);
      });
//      row.onclick = () => openProfile(s);
      table.appendChild(row);
    });
  })
  .catch(() => {
    window.location.href = "/dashboard.html";
  });

function vacate(id) {
  if (!confirm("Vacate this student?")) return;

   console.log("Vacate response status:", id);
  fetch(`/api/student/${id}/vacate`, { method: "POST" })
    .then(() => location.reload());
}

function formatDate(date) {
  return new Date(date).toLocaleDateString();
}


function openEditModal(s) {
  document.getElementById("editId").value = s.id;
  document.getElementById("editName").value = s.name;
  document.getElementById("editPhone").value = s.phone;
  document.getElementById("editSeat").value = s.seatNumber;

  if (s.endDate) {
    document.getElementById("editEndDate").value =
      s.endDate.split("T")[0];
  }

  document.getElementById("editModal").classList.remove("hidden");
}

function closeModal() {
  document.getElementById("editModal").classList.add("hidden");
}


function saveStudent() {
  const id = document.getElementById("editId").value;

  const payload = {
    name: document.getElementById("editName").value,
    phone: document.getElementById("editPhone").value,
    seatNumber: parseInt(document.getElementById("editSeat").value),
    endDate: document.getElementById("editEndDate").value + "T00:00:00"
  };

  fetch(`/api/students/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
  .then(() => location.reload());
}

//search student / Filter student
function searchStudents() {
  const name = document.getElementById("searchName").value;
  const phone = document.getElementById("searchPhone").value;
  const seat = document.getElementById("searchSeat").value;

  let url = "/api/student/search?";
  if (name) url += `name=${name}&`;
  if (phone) url += `phone=${phone}&`;
  if (seat) url += `seat=${seat}`;

  fetch(url)
    .then(res => res.json())
    .then(renderStudentTable);
}



function openProfile(s) {
  document.getElementById("studentProfile").innerHTML = `
    <div class="profile">
      <h3>${s.name}</h3>
      <p>${s.phone}</p>

      <hr>

      <p><b>Seat:</b> ${s.seatNumber}</p>
      <p><b>Joined:</b> ${formatDate(s.startDate)}</p>
      <p><b>Expires:</b> ${formatDate(s.endDate)}</p>

      <p class="badge ${s.active ? "active" : "expired"}">
        ${s.active ? "Active" : "Expired"}
      </p>
    </div>
  `;
}



function loadStudentProfile(s) {
  const panel = document.getElementById("studentProfile");
  panel.classList.remove("hidden");

  document.getElementById("pName").innerText = s.name;
  document.getElementById("pPhone").innerText = s.phone;
  document.getElementById("pSeat").innerText = s.seatNumber;

  document.getElementById("pJoined").innerText =
    s.startDate ? formatDate(s.startDate) : "-";

  document.getElementById("pExpire").innerText =
    s.endDate ? formatDate(s.endDate) : "-";
}