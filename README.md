# file-safe
Зурган дотор мэдээлэл нууцлагч

I.	JavaFX нь янз бүрийн төхөөрөмжөөс хандах боломжтой Oracle – ийн хөгжүүлсэн програм хангамжийн платформ юм. Хөгжүүлэлтэнд JavaFX ашиглах гол шалтгаан нь нэг бичигдсэн код нь desktop, android, ios, web зэрэг бүх төрлийн төхөөрөмжид ашиглагдаж болдог нь давуу талтай юм. Жава програмчлалын хэлийг ашиглан GUI програмуудыг боловсруулахын тулд ашигладаг ба бидний application-д хэрэглэгчийн харагдах хэсгийг хийхэд ашигласан.

II.	Javax нь криптографийн үйл ажиллагааны анги/class/ болон интерфейсийг агуулдаг. Энэ багцад тодорхойлогдсон криптографийн үйлдлүүд нь шифрлэлт, түлхүүр үгс, мессежийг баталгаажуулах код/MAC/ үүсгэдэг.
Түлхүүр гаргаж авах – Javax Crypto KeyGenerator санг ашиглан 16 тэмдэгтийн урттай 128 битийн түлхүүрийг гарган авсан.
1.	KeyGenerator keyGen = KeyGenerator.getInstance("AES");  
2.	keyGen.init(128);  
3.	SecretKey secretKey = keyGen.generateKey();  

AES encode хийх – Javax Crypto Cipher санг ашиглан өгөгдсөн текстэд AES алгоритмаар нууцлана. Ингэхдээ нууц түлхүүрийг гаргаж авсаны дараа cipher төрлийн объект үүсгэж AES алгоритм болон нууцлах түлхүүрийг зааж өгч нууцлана.
1.	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
2.	cipher.init(Cipher.ENCRYPT_MODE, aesKey);  
3.	return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));        

AES decode хийх - Javax Crypto Cipher санг ашиглан өгөгдсөн нууцлагдсан мэдээллээс AES алгоритмаар гаргаж авна. Ингэхдээ нууц түлхүүрийг хэрэглэгчээс авсаны дараа cipher төрлийн объект үүсгэж AES алгоритм болон нууцалсан түлхүүрийг зааж өгч мэдээллийг авна.
1.	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");  
2.	cipher.init(Cipher.DECRYPT_MODE, aesKey);  
3.	return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));  

III.	Шифрлэсэн текстээ зурганд нуух, тайлах - Параметрээр зургаа авч түүнтэй ижил WritableImage төрлийн объект үүсгэнэ үүний учир нь нууцалсан мэдээллээ хадгалах зориулалттай. Мэдээллээ нууцлахдаа pixel – ээр нь уншиж нууцлах учир PixelReader болон PixelWriter – ийг ашиглан зургаа pixel болгон уншина. Зургандаа мэдээллээ нууцлахдаа мэдээллээ эхлээд шифрлээд шифрлэсэн өгөгдөлөө битийн урсгал болгож зурагны pixel – үүдийн сүүлийн 2 битийг өөрийн нууц мэдээлэлээр /битийн цуваа/ солих замаар мэдээллээ зурганд нууцалдаг. Жишээ нь : “abcd” текст нь decimal – ын [97, 98, 99, 100] тоотой тэнцүү бөгөөд битийн цуваа нь 1100001, 1100010, 1100011, 1100100 болох ба нуух зурагны pixel – үүдийн сүүлийн 1 битийг дарж өөрийн мэдээллээ бичдэг. Энэ нь хүний нүд, зурагны чанарт мэдэгдэхүйц нөлөө үзүүлдэггүй юм.

Эхний зураг нь текст нууцлаагүй байхад дараагийнх нь мэдээлэл нууцалсны дараах байдал.
IV.	Мэдээлэл нууцлах – Нууцлах мэдээллээ текст бичих талбайд бичиж өгсөны дараагаар зураг сонгох хэсгээс мэдээлэл нууцлах зургаа согож өгөх хэрэгтэй. Дээрх хоёр алхмын дараа нууцлах товчлуур дээр дарснаар нууцлагдсан зургаа хадгалах замаа зааж өгөх хэрэгтэй. Хадгалсны дараа танд доод талын талбайд текст encryption хийсэн түлхүүр үгийг гаргаж өгөх ба түүнийг өөртөө хадгалах хэрэгтэй юм.

V.	Мэдээлэл тайлах – Зураг сонгох товчлуурыг дарснаар нууцлагдсан өгөгдөлтэй зургаа сонгож өгөх хэрэгтэй ба түүний доод хэсэгт байгаа текст талбай дээр encryption хийсэн түлхүүр үгээ өгснөөр танд нууцалсан мэдээллийг тайлж өгөх юм.