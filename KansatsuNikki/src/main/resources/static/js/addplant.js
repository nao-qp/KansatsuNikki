'use strict';
/**
 * 植物画像のアップロード
 */

const fileInput = document.getElementById('file-input');
const photoPreviewContainer = document.getElementById('photo-preview-container');
const photoSlots = document.querySelectorAll('.photo-slot');

// 画像追加ボタンのクリックイベント
document.getElementById('add-photo-button').onclick = function() {
    fileInput.click();
    
};

//TODO:ファイル選択方法を、ドロップにも対応する。（Ver.2以降実装検討）


// ファイルが選択されたときの処理
//fileInput.onchange = function(event) {
fileInput.addEventListener('change', function(event) {
    const files = Array.from(event.target.files);
    let imagesSelected = false; // 画像が選択されたかどうかを管理

    // スロットの状態を確認し、空いているスロットを見つける
    const availableSlots = Array.from(photoSlots).filter(slot => slot.querySelector('img').style.display === 'none');


    // 選択されたファイルをスロットに表示
    files.forEach((file, index) => {
        if (index < availableSlots.length) {
            const reader = new FileReader();
            const imgSlot = availableSlots[index];
            const img = imgSlot.querySelector('img');
            const removeBtn = imgSlot.querySelector('.remove-btn');
            const addPhotoBtn = document.getElementById('add-photo-button');	//ファイル追加ボタン制御用

			//画像ファイル読み込み実行後(※1実行後)に、実行される
            reader.onload = function(e) {
                img.src = e.target.result;  // 画像データのURL
                img.style.display = 'block'; // 画像を表示
                imgSlot.style.display = 'flex'; // スロットを表示
                removeBtn.style.display = 'block'; // バツボタンを表示
                // 画像が選択されたフラグを立てる(画像表示エリア全体の表示可否を判定)
                imagesSelected = true;
                
                  // photo-preview-containerの表示を制御
                    photoPreviewContainer.style.display = imagesSelected ? 'block' : 'none';
                    
                  ////追加ボタンを無効にする
                  	//画像がセットされているスロットを取得する
			        const resultAvailableSlots = Array.from(photoSlots).filter(slot => slot.querySelector('img').style.display === 'block');
			        console.log(resultAvailableSlots.length);
			        console.log(availableSlots.length);
			        //4つ埋まっていたら、追加ボタンを無効にする
			        if (resultAvailableSlots.length >= 4) {
						addPhotoBtn.style.pointerEvents = 'none'; // ボタンを無効にする
            			addPhotoBtn.style.opacity = '0.5'; // 見た目を変更
					}
					
				  // ファイル入力をリセット
                  //fileInput.value = ''; // 読み込み完了後にリセット
            };
			
			
            // バツボタンのクリックイベント
            removeBtn.onclick = function() {
                img.style.display = 'none'; // 画像を非表示
                imgSlot.style.display = 'none'; // スロットを非表示
                removeBtn.style.display = 'none'; // バツボタンを非表示
                imagesSelected = false; // 画像が選択されていないフラグを立てる

				//無効にしていた追加ボタンを元に戻す
				addPhotoBtn.style.pointerEvents = 'auto'; // ボタンを有効にする
    			addPhotoBtn.style.opacity = '1'; // 見た目を初期値にする
					

                // TODO:画像を詰める処理（Ver.2以降実装検討）
                //①style.display === 'block'のものを取得
                //②スロットを一度空にする。
                //③①で取得した要素を詰め直す。
                
                
            };
            
            //(※1)画像ファイル読み込み
            reader.readAsDataURL(file); // 画像ファイルをDataURLとして読み込む
            
        }
        
    });
    
});


// 更新ボタン押下
// フォーム送信時にボタンを無効化
document.getElementById('addplant-form').addEventListener('submit', function() {
    const button = document.getElementById('update-btn');
    button.disabled = true;
    button.textContent = messages.btnProcessing;
   
});


