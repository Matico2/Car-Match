import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carmatch.model.Vehicle
import com.example.carmatch1.databinding.ItemVehicleFavBinding

class FavVehicleAdapter(private val itemClickListener: OnClickListener) :
    RecyclerView.Adapter<FavVehicleAdapter.VehiclesViewHolder>() {
    
    interface OnClickListener {
        fun onVehicleClick(vehicleId: String)
        fun onRemoveFavoriteClick(vehicleId: String)
        fun onChatClick(chatId: String)
    }
    
    private var listFavVehicle = listOf<Vehicle>()
    
    fun addList(list: List<Vehicle>) {
        listFavVehicle = list
        notifyDataSetChanged()
    }
    
    inner class VehiclesViewHolder(private val binding: ItemVehicleFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(vehicle: Vehicle) {
            binding.txtModel.text = vehicle.model
            binding.txtBrand.text = vehicle.brand
            binding.txtPrice.text = vehicle.price.toString()
            
            binding.heartIcon.setOnClickListener {
                itemClickListener.onRemoveFavoriteClick(vehicle.vehicleId)
            }
            
            binding.btnStartConversation.setOnClickListener {
                itemClickListener.onChatClick(vehicle.vehicleId)
            }
            
            itemView.setOnClickListener {
                itemClickListener.onVehicleClick(vehicle.vehicleId)
            }
        }
    }
    
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVehicleFavBinding.inflate(inflater, parent, false)
        return VehiclesViewHolder(binding)
    }
    
    override fun getItemCount(): Int = listFavVehicle.size
    
    override fun onBindViewHolder(holder: VehiclesViewHolder, position: Int) {
        holder.bind(listFavVehicle[position])
    }
}
