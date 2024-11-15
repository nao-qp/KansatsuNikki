'use strict';
/**
 * 植物編集
 */

const fileInput = document.getElementById('file-input');
const photoPreviewContainer = document.getElementById('photo-preview-container');
const photoSlots = document.querySelectorAll('.photo-slot');
const addPhotoBtn = document.getElementById('add-photo-button');

//HTMLが読み込まれた時に実行
document.addEventListener("DOMContentLoaded", function() {
	 setStyleBlockFunction();
});

//登録済み画像データがセットされていたらdisplay:blockを設定して表示する
function setStyleBlockFunction() {
	
	////blockが設定されているスロットがあるかどうか確認する
	//画面初期表示時にblockが設定されているスロットのリスト
	const blockSlots = Array.from(photoSlots).filter(slot => slot.querySelector('img').style.display === 'block');
	
	if (blockSlots.length > 0) {
		//画像表示エリアのstyleをblockに設定して表示する
		photoPreviewContainer.style.display = 'block';
		
		//バツボタン表示設定
		//既存データを表示した各スロットに対して設定していく
		blockSlots.forEach((photoSlot) => {
			const imgSlot = photoSlot;
			const img = imgSlot.querySelector('img');
			const removeBtn = photoSlot.querySelector('.remove-btn');
			removeBtn.style.display = 'block';
			// バツボタンのクリックイベントを設定
			removeBtn.onclick = () => removeBtnClick(img, imgSlot, removeBtn);
		});
	}
	
	//スロットが画像で埋まっているか確認し追加ボタンの表示を設定する
	if (blockSlots.length >= slotNum) {
		addPhotoBtn.style.pointerEvents = 'none';
		addPhotoBtn.style.opacity = '0.5';
	}
}

// 画像追加ボタンのクリックイベント
document.getElementById('add-photo-button').onclick = function() {
    fileInput.click();
};

// ファイルが選択されたときの処理
fileInput.addEventListener('change', function(event) {
    const files = Array.from(event.target.files);
//    let imagesSelected = false; // 画像が選択されたかどうかを管理

    // スロットの状態を確認し、空いているスロットを見つける
    const availableSlots = Array.from(photoSlots).filter(slot => slot.querySelector('img').style.display === 'none');

    // 選択されたファイルをスロットに表示
    files.forEach((file, index) => {
        if (index < availableSlots.length) {
            const reader = new FileReader();
            const imgSlot = availableSlots[index];
            // 各スロットの要素
            const img = imgSlot.querySelector('img');
            const removeBtn = imgSlot.querySelector('.remove-btn');
            
			//画像ファイル読み込み実行後(※1実行後)に、実行される
            reader.onload = function(e) {
                img.src = e.target.result;  // 画像データのURL
                img.style.display = 'block'; // 画像を表示
                imgSlot.style.display = 'flex'; // スロットを表示
                removeBtn.style.display = 'block'; // バツボタンを表示
                // 画像が選択されたフラグを立てる(画像表示エリア全体の表示可否を判定)
//                imagesSelected = true;
                
                  // photo-preview-containerの表示を制御
//                    photoPreviewContainer.style.display = imagesSelected ? 'block' : 'none';
					//ファイル読み込みがあったこの時点では必ずblockとするはずなので、判定不要？
                    photoPreviewContainer.style.display = 'block';
                    
                  ////追加ボタンを無効にする
                  	//画像がセットされているスロットを取得する
			        const resultAvailableSlots = 
			        		Array.from(photoSlots).filter(slot => slot.querySelector('img').style.display === 'block');
			        //全てのスロットが埋まっていたら、追加ボタンを無効にする
			        if (resultAvailableSlots.length >= slotNum) {
						addPhotoBtn.style.pointerEvents = 'none'; // ボタンを無効にする
            			addPhotoBtn.style.opacity = '0.5'; // 見た目を変更
					}
					
				  // ファイル入力をリセット
                  //fileInput.value = ''; // 読み込み完了後にリセット
            };
			
            // バツボタンのクリックイベントを設定
            removeBtn.onclick = () => removeBtnClick(img, imgSlot, removeBtn);
//            removeBtn.onclick = function() {
//                img.style.display = 'none'; // 画像を非表示
//                imgSlot.style.display = 'none'; // スロットを非表示
//                removeBtn.style.display = 'none'; // バツボタンを非表示
////                imagesSelected = false; // 画像が選択されていないフラグを立てる
//
//				//無効にしていた追加ボタンを元に戻す
//				addPhotoBtn.style.pointerEvents = 'auto'; // ボタンを有効にする
//    			addPhotoBtn.style.opacity = '1'; // 見た目を初期値にする
//					
//
//                // TODO:画像を詰める処理（Ver.2以降実装検討）
//                //①style.display === 'block'のものを取得
//                //②スロットを一度空にする。
//                //③①で取得した要素を詰め直す。
//                
//            };
            
            //(※1)画像ファイル読み込み
            reader.readAsDataURL(file); // 画像ファイルをDataURLとして読み込む
            
        }
    });
});

 // バツボタンクリック時の動作
 function removeBtnClick(img, imgSlot, removeBtn) {
	img.style.display = 'none'; // 画像を非表示
    imgSlot.style.display = 'none'; // スロットを非表示
    removeBtn.style.display = 'none'; // バツボタンを非表示
    
	//無効にしていた追加ボタンを元に戻す
	addPhotoBtn.style.pointerEvents = 'auto'; // ボタンを有効にする
	addPhotoBtn.style.opacity = '1'; // 見た目を初期値にする
 };


