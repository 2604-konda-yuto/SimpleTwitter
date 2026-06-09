// メールを作る関数
let createMail = ( rec, bill ) => {
	let msg = `${ rec }様
	お疲れさまです。経理部です。
	今月の請求金額は${ bill }円です。`
	console.log( msg )
};

// 消費税を追加する関数
let addCharge = ( bill ) => {
	return bill * 1.08;
};

// 送付先データ
let data = [
	{ name:`宮下`, bill:4000, flag:true },
	{ name:`村上`, bill:8000, flag:false },
	{ name:`小菅`, bill:5000, flag:false },
	{ name:`下川`, bill:10000, flag:true }
];

// 実行部分
for ( let rec of data ) {
	let bill = rec['bill'];
	if( rec['flag'] ) {
		bill = addCharge( bill );
	}

	createMail( rec['name'], bill );
}