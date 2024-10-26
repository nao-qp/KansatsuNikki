'use strict';
/**
 * 植物削除確認モーダル
 */

let itemIdToDelete;

function showDeleteModal(itemId, deletePlantName) {
    itemIdToDelete = itemId;
   var modalElement = document.getElementById('deleteModal');
    
    // Bootstrapのモーダルインスタンスを作成
    var modal = new bootstrap.Modal(modalElement);
    
    // 植物の名前を設定
    
    
    
    // モーダルを表示
    modal.show();
}

//モーダルの削除ボタンから削除処理のForm
document.getElementById('confirmDeleteButton').onclick = function() {
    document.getElementById('deleteForm' + itemIdToDelete).submit();
};