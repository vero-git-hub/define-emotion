function confirmDelete(event) {
    if (!confirm("Are you sure you want to delete this emotion?")) {
        event.preventDefault(); 
        console.log("Deletion cancelled");
    } else {
        console.log("Deletion confirmed");
    }
}

$(document).ready(function () {
    $('.delete-form').on('submit', function (event) {
        if (!confirm("Are you sure you want to delete this emotion?")) {
            event.preventDefault();
        }
    });
    
    $('#emotionsTable').DataTable({
        paging: true,
        searching: true,
        ordering: true,
        order: [[0, 'desc']],
        lengthMenu: [5, 10, 25],
        language: {
            search: "Filter emotions:",
            lengthMenu: "Show _MENU_ entries",
            info: "Showing _START_ to _END_ of _TOTAL_ emotions",
            emptyTable: "No emotions found."
        }
    });
});