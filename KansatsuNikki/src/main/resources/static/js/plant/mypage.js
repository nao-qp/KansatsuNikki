'use strict';
/**
 * 植物削除確認モーダル
 */

let itemIdToDelete;

//モーダル表示
function showDeleteModal(itemId, deletePlantName) {
    itemIdToDelete = itemId;
   var modalElement = document.getElementById('deleteModal');
    
    // Bootstrapのモーダルインスタンスを作成
    var modal = new bootstrap.Modal(modalElement);
    
    // 植物の名前を設定
    //モーダルのテキスト要素を取得
    var plantNameToDelete_text = document.getElementById('plantNameToDelete');
    //モーダルに植物名をセット
    plantNameToDelete_text.textContent = deletePlantName;
    
    // モーダルを表示
    modal.show();
}


//モーダルの削除ボタンから削除処理のFormのsubmitを実行
document.getElementById('confirmDeleteButton').onclick = function() {
    document.getElementById('deleteForm' + itemIdToDelete).submit();
};
