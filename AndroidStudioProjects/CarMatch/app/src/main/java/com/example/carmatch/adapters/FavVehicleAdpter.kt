import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.FavVehicle
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.databinding.ItemVehicleFavBinding

class FavVehicleAdapter(
    private var listFavVehicle: MutableList<FavVehicle>,
    private val listVehicle: List<Vehicle>,
    private val itemClickListener: OnClickListener
) : RecyclerView.Adapter<FavVehicleAdapter.FavVehiclesViewHolder>() {
    
    interface OnClickListener {
        fun onVehicleClick(vehicleId: String)
        fun onRemoveFavoriteClick(vehicleId: String)
        fun onChatClick(chatId: String)
    }
    
    inner class FavVehiclesViewHolder(private val binding: ItemVehicleFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(favVehicle: FavVehicle) {
            val vehicle = listVehicle.find { it.vehicleId == favVehicle.idVehicle }
            if (vehicle != null) {
                binding.txtModel.text = vehicle.model
                binding.txtBrand.text = vehicle.brand
                binding.txtPrice.text = "R$ ${vehicle.price}"
            } else {
                binding.txtModel.text = "Modelo indisponível"
                binding.txtBrand.text = "Marca desconhecida"
                binding.txtPrice.text = "--"
            }
            
            binding.heartIcon.setOnClickListener {
                itemClickListener.onRemoveFavoriteClick(favVehicle.idVehicle)
            }
            
            binding.btnStartConversation.setOnClickListener {
                itemClickListener.onChatClick(favVehicle.idVehicle)
            }
            
            itemView.setOnClickListener {
                itemClickListener.onVehicleClick(favVehicle.idVehicle)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavVehiclesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVehicleFavBinding.inflate(inflater, parent, false)
        return FavVehiclesViewHolder(binding)
    }
    
    override fun getItemCount(): Int = listFavVehicle.size
    
    override fun onBindViewHolder(holder: FavVehiclesViewHolder, position: Int) {
        val favVehicle = listFavVehicle[position]
        holder.bind(favVehicle)
    }
    
    fun updateList(favList: List<FavVehicle>) {
        listFavVehicle.clear()
        listFavVehicle.addAll(favList)
        notifyDataSetChanged()
    }
}
