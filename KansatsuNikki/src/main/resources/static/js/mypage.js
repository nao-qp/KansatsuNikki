'use strict';
/**
 * 植物削除確認モーダル
 */

let itemIdToDelete;

function showDeleteModal(itemId) {
    itemIdToDelete = itemId;
   var modalElement = document.getElementById('deleteModal');
    
    // Bootstrapのモーダルインスタンスを作成
    var modal = new bootstrap.Modal(modalElement);
    
    // モーダルを表示
    modal.show();
}

//モーダルの削除ボタンから削除処理のForm
document.getElementById('confirmDeleteButton').onclick = function() {
    document.getElementById('deleteForm' + itemIdToDelete).submit();
};