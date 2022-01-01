package bot.inker.core.command

import bot.inker.api.command.UUIDValueType
import com.eloli.inkcmd.StringReader
import com.eloli.inkcmd.exceptions.DynamicCommandExceptionType
import java.util.*
import javax.inject.Singleton

class InkUUIDValueType:UUIDValueType{
    override fun parse(reader: StringReader): UUID {
        val input = reader.readString()
        if(input.length > 38) {
            throw UNEXPECTED_UUID.createWithContext(reader,input)
        }
        if((input.length > 36 || input[0] == '[') &&
            (input[0] != '[' || input[input.length-1] != ']')
        ){
            throw UNEXPECTED_UUID.createWithContext(reader,input)
        }
        try {
            return if(input[0] == '['){
                UUID.fromString(input.substring(1,input.length-1))
            }else{
                UUID.fromString(input)
            }
        }catch (e:IllegalArgumentException){
            throw UNEXPECTED_UUID.createWithContext(reader,input)
        }
    }
    companion object{
        val UNEXPECTED_UUID = DynamicCommandExceptionType{ input->
            "Expected uuid, got $input"
        }
    }
    @Singleton
    class Factory: UUIDValueType.Factory{
        override fun of(): UUIDValueType {
            return InkUUIDValueType()
        }
    }
}