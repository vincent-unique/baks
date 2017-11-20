/**
 * Created by Vincent on 2017/8/31 0031.
 */


$(function () {
    // select all tables
    // select all tables
    $("#selection-all-tables").change(function(){
        var checkedStatus = $(this).prop('checked');

        $.each($("#selection-all-tables").closest('table').find('.selection'), function() {
            $(this).prop('checked', checkedStatus);
            if (checkedStatus) {
                $(this).closest('span').removeClass('checked').addClass('checked');
            }else {
                $(this).closest('span').removeClass('checked');
            }

        });

    });

    // Handle migration button click event
    $('#migrate').click(function() {
        // mark tables as red
        $.each($('.selection .checked'), function() {
            if (!$(this).closest('tr').hasClass('success')){
                $(this).closest('tr').addClass('danger');
            }
        });

        // show loader
        $('#loader').css('display', 'block');

        var selectedTables = [];

        // populate selected tables array
        $.each($('.selection'), function() {
            if ($(this).prop('checked')) {
                var $self = $(this);
                var tableName = $self.closest('tr').find('.table-name').attr('title');
                var entry = {
                    table: tableName,
                    context: $self
                };
                selectedTables.push(entry);
            }
        });

        $.each(selectedTables, function() {
            logger.info(this.table);
        });


        // Tame Ajax asynchronous requests. Make multiple Ajax asynchronous requests executed in order, which
        // means in a synchronous manner..
        var migrationHandler = function (tables) {

            if (tables.length !== 0){
                var entry = tables.shift();

                var paras = {table: JSON.stringify({table: entry['table']})};

                $.ajax({
                    url: 'migrate',
                    type: 'POST',
                    data: paras,
                    dataType: 'json'

                }).always(function(response) {
                    if(response.size){
                        var $self = entry['context'];
                        $self.closest('tr').find('.record-counter').text(response.size);
                        $self.closest('tr').find('.selection').prop('checked', false);
                        $self.closest('tr').find('.selection').closest('span').removeClass('checked');
                        $self.closest('tr').removeClass('danger').addClass('success');

                    }
                    migrationHandler(tables);
                });
            }

            // remove loader
            if (tables.length === 0) {
                $('#loader').css('display', 'none');
            }

        };

        migrationHandler(selectedTables);

    });

    $('#disconnect').on('click', function() {
        $.ajax({
            url: 'disconnect'
        }).always(function() {
            window.location.href = "home";
        });
    });
});