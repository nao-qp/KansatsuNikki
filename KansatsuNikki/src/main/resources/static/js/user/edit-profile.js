/**
 * プロフィール編集
 */
const container = document.getElementById('photo-preview-container').querySelector('div.d-flex');
const fileInput = document.getElementById('file-input');
const addPhotoButton = document.getElementById('add-photo-button');
const updateButton = document.getElementById('update-btn');

let selectedFiles = []; // 新規追加ファイルリスト
let deletedFilePaths = [];    // 削除対象IDリスト

// ファイル追加ボタン → ファイル選択を開く
addPhotoButton.addEventListener('click', () => fileInput.click());

// ファイル選択時
fileInput.addEventListener('change', function (event) {
  	const file = event.target.files[0];
  	if (!file) return; // ファイルが選ばれていなければ何もしない
  	
  	// 新しい画像の表示div要素をtemplateから作成
  	const template = document.getElementById('photo-slot-template').content.cloneNode(true);
  	if (!template) {
    console.error('テンプレートが見つかりません');
    return;
  	}
  	
  	// 画像の大きさチェック
	if (file.size > 10 * 1024 * 1024) { // 2MB制限
	  alert('ファイルサイズが大きすぎます！（最大2MB）');
	  return;
	}
	
  	// 既存画像があれば削除対象に
	const oldSlot = container.querySelector('.photo-slot');
	if (oldSlot) {
	  const filePath = oldSlot.getAttribute('data-filePath');
	  if (filePath && !filePath.startsWith('temp-')) {
	    deletedFilePaths = [filePath];
	   }
	}
	
	// 新しいファイルで上書きなのでクリア
	  selectedFiles = [];
	  container.innerHTML = '';
  
  	// 新しく選択した画像はtempFilePathを設定
  	const tempFilePath = `temp-${Date.now()}-${Math.random()}`;
    // selectedFiles: { file: File, tempFilePath: string }[]
    selectedFiles.push({ file, tempFilePath });
    

//  const template = document.getElementById('photo-slot-template').content.cloneNode(true);
  const slot = template.querySelector('.photo-slot');
  slot.setAttribute('data-filePath', tempFilePath);

  const img = slot.querySelector('img');
  const reader = new FileReader();
  // 処理順② 読み込んだらimgにセット(ファイル読み込みを行った後に実行する処理を定義しておく)
  reader.onload = e => img.src = e.target.result;
  // 処理順① ファイルをDataURL形式にして読み込み
  reader.readAsDataURL(file);

  // 画像をセットしたslotをプレビュー表示エリアに追加
  container.appendChild(slot);
  fileInput.value = '';	// リセットして同じファイルも再選択できるようにする
  
});

// 削除ボタン押下
container.addEventListener('click', function (e) {
  if (e.target.closest('.remove-btn')) {
    const slot = e.target.closest('.photo-slot');
    const filePath = slot.getAttribute('data-filePath');
    if (filePath && !filePath.startsWith('temp-')) {
      deletedFilePaths.push(filePath); // 既存データなら削除リストへ
    }
    slot.remove();
  }
});

// 更新ボタン押下時
updateButton.addEventListener('click', async () => {
	 console.log('更新ボタンが押されました');
	
	// ボタンを無効化して「処理中」に変更
	updateButton.disabled = true;
	updateButton.textContent = '処理中';
	
  // コントローラーに送るフォームデータを作成する
  const formData = new FormData();
  // 削除対象のファイルパス
  formData.append('deletedFilePaths', deletedFilePaths);
  // ニックネームとプロフィール内容をセット
  const nickname = document.getElementById('nickname').value;
  const profile = document.getElementById('profile').value;
  formData.append('nickname', nickname);
  formData.append('profile', profile);
  // ファイルをセット
  if (selectedFiles.length > 0) {
  const { file, tempFilePath } = selectedFiles[0];
  formData.append('file', file);
  formData.append('tempFilePath', tempFilePath);
}


// デバッグ用
console.log('FormDataの中身:');
for (const [key, value] of formData.entries()) {
  if (value instanceof File) {
    console.log(`${key}: [ファイル] ${value.name}, ${value.size}バイト, ${value.type}`);
  } else {
    console.log(`${key}:`, value);
  }
}

  // コントローラーへリクエストを送信
  const userId = document.getElementById('user-id').value;
  try {
    const response = await fetch(`/user/edit-profile/${userId}`, {
      method: 'POST',
      body: formData
    });
    const result = await response.json();
    console.log('完了:', result);
    // 成功時のリダイレクト処理
    if (!response.ok) {
		console.error('サーバーエラー:', result);
	} else {
	  // 成功した場合
	  if (result.redirectUrl) {
	    window.location.href = result.redirectUrl;	// マイページ
	  }
	}
    
  } catch (error) {
    console.error('エラー:', error);
  }
});