package csoundo;

class Cast<T extends Number> {
   
    public float toFloat(T t) {        
        return t.floatValue();
    }
    
    public T toCast(Number n){
        return (T) n;
    }
}