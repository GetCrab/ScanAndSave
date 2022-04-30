package hr.bm.scanandsave.api.responses

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@SerializedName("user_id")
	val userId: String? = null,

	@SerializedName("acounts")
	val accounts: List<AccountsItem?>? = null
)

data class AccountsItem(

	@SerializedName("amount")
	val amount: String,

	@SerializedName("IBAN")
	val iban: String,

	@SerializedName("id")
	val id: String,

	@SerializedName("transactions")
	val transactions: List<TransactionsItem?>? = null
)

data class TransactionsItem(

	@SerializedName("date")
	val date: String,

	@SerializedName("amount")
	val amount: String,

	@SerializedName("description")
	val description: String,

	@SerializedName("id")
	val id: String,

	@SerializedName("type")
	val type: String? = null
)
