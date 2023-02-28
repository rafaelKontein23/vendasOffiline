package visaogrupo.com.br.modulo_visitacao.Views.Models.Ultis

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat

class Trocar_cor_de_icon {

    public fun trocar_cor_iten(icon: ImageView, drawable: Drawable, cor:String){
        val wrappedDrawable = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(cor))
        icon.setImageDrawable(wrappedDrawable)
    }
}