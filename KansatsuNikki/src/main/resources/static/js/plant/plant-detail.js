'use strict';
/**
 * 観察日記削除モーダル
 */

let itemIdToDelete;

 //モーダル表示
function showDeleteModal(itemId, deleteObservationDate) {
    itemIdToDelete = itemId;
   var modalElement = document.getElementById('deleteModal');
    
    // Bootstrapのモーダルインスタンスを作成
    var modal = new bootstrap.Modal(modalElement);
    
    // 観察日を設定
    //モーダルのテキスト要素を取得
    var diaryToDelete_text = document.getElementById('diaryToDelete');
    //モーダルに観察日をセット
    diaryToDelete_text.textContent = deleteObservationDate;
    
    // モーダルを表示
    modal.show();
}

//モーダルの削除ボタンから削除処理のFormのsubmitを実行
document.getElementById('confirmDeleteButton').onclick = function() {
    document.getElementById('deleteForm' + itemIdToDelete).submit();
};
