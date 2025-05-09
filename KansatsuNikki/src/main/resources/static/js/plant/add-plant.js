'use strict';
/**
 * 植物画像のアップロード
 */


const container = document.getElementById('photo-preview-container').querySelector('div.d-flex');
const fileInput = document.getElementById('file-input');
const addPhotoButton = document.getElementById('add-photo-button');
const updateButton = document.getElementById('update-btn');

let selectedFiles = []; // 新規追加ファイルリスト

// ファイル追加ボタン → ファイル選択を開く
addPhotoButton.addEventListener('click', () => fileInput.click());

// ファイルが選ばれたとき
fileInput.addEventListener('change', function (event) {
  const files = Array.from(event.target.files);
  
  const slots = container.querySelectorAll('.photo-slot');
  const currentSlotCount = slots.length;

  if (currentSlotCount >= slotNum) {
    alert(`画像は最大${slotNum}枚までしか選べません。`);
    fileInput.value = ''; // リセットしておく
    return;
  }

  files.forEach(file => {
	 // 画像追加数チェック
	 if (container.querySelectorAll('.photo-slot').length >= slotNum) {
      alert(`画像は最大${slotNum}枚までです。`);
      return; // これ以上追加しない
    }
	// 画像の大きさチェック
    if (file.size > 10 * 1024 * 1024) { // 2MB制限
      alert('ファイルサイズが大きすぎます！（最大2MB）');
      return;
    }
    const tempId = `temp-${Date.now()}-${Math.random()}`;
    // selectedFiles: { file: File, tempId: string }[]
    selectedFiles.push({ file, tempId });

    const template = document.getElementById('photo-slot-template').content.cloneNode(true);
    const slot = template.querySelector('.photo-slot');
    slot.setAttribute('data-id', tempId);

	// ファイルを読み込んで画面に表示する
    const img = slot.querySelector('img');
    const reader = new FileReader();
    // 処理順② 読み込んだらimgにセット(ファイル読み込みを行った後に実行する処理を定義しておく)
    reader.onload = e => img.src = e.target.result;
    // 処理順① ファイルをDataURL形式にして読み込み
    reader.readAsDataURL(file);

	// 画像をセットしたslotをプレビュー表示エリアに追加
    container.appendChild(slot);
  });

  fileInput.value = ''; // リセットして同じファイルも再選択できるようにする
  document.getElementById('photo-preview-container').style.display = 'block'; // 表示
});

// 削除ボタン押下
container.addEventListener('click', function (e) {
  if (e.target.closest('.remove-btn')) {
    const slot = e.target.closest('.photo-slot');
    slot.remove();
  }
});

// 並び替え可能にする
new Sortable(container, { animation: 150 });

// 更新ボタン押下時
updateButton.addEventListener('click', async () => {
	 console.log('更新ボタンが押されました'); // ← これ表示されるか確認

	// ボタンを無効化して「処理中」に変更
	updateButton.disabled = true;
	updateButton.textContent = '処理中';

  const slots = Array.from(container.querySelectorAll('.photo-slot'));
  const orderedIds = slots.map(slot => slot.getAttribute('data-id'));
  // コントローラーに送るフォームデータを作成する
  // 並んでいるスロットのIDと、削除対象のIDの配列をセット
  const formData = new FormData();
  formData.append('orderedIds', JSON.stringify(orderedIds));
  
  // 植物の名前と詳細をセット
  const name = document.getElementById('name').value;
  const detail = document.getElementById('detail').value;
  formData.append('name', name);
  formData.append('detail', detail);
  // 
  selectedFiles.forEach(({ file, tempId }) => {
    formData.append('files', file);
    formData.append('tempIdList', tempId);
  });

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
  try {
    const response = await fetch('/plant/add', {
      method: 'POST',
      body: formData
    });
    const result = await response.json();
    console.log('完了:', result);
    // 成功時のリダイレクト処理
    if (!response.ok) {
	  if (response.status === 400) {
	    // バリデーションエラー処理
	    console.error('バリデーションエラー:', result.errors);
	    //TODO: 各項目にエラーメッセージを表示するなど
	    
	  } else {
	    // その他のエラー
	    console.error('サーバーエラーなど:', result);
	  }
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
