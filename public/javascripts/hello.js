$(document).ready(function () {
    var location = window.location.pathname;
    var dashboard = $('#dashboard');
    var categories = $('#category-dropdown');
    var tasks = $('#tasks-dropdown');
    dashboard.removeClass('active');
    categories.removeClass('active');
    tasks.removeClass('active');
    if (location.startsWith('/task')) {
        tasks.addClass('active');
    } else if (location.startsWith('/category')) {
        categories.addClass('active');
    } else {
        dashboard.addClass('active');
    }
});

function confirmDeleteCategory() {
    return window.confirm('Are you sure you want to delete this category? This will also delete all tasks within.');
}

function confirmDeleteTask() {
    return window.confirm('Are you sure you want to delete this task?');
}