$(document).ready(function () {
    $('.table').DataTable({
        paging: true,
        searching: true,
        ordering: true,
        lengthMenu: [5, 10, 25],
        language: {
            search: "Filter emotions:",
            lengthMenu: "Show _MENU_ entries",
            info: "Showing _START_ to _END_ of _TOTAL_ emotions",
            emptyTable: "No emotions found."
        }
    });
});