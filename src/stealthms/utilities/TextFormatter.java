package stealthms.utilities;

import stealthms.storage.OptionsStorage;

public class TextFormatter {
	public String translit(String text, boolean utf) {
		if (OptionsStorage.getTranslitStat() == 0) {
			if (utf==true) {
				return Utf2Ascii(text);
			} else {
				return text;
			}
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < text.length(); i++) {
				char ch = text.charAt(i);
				switch (ch) {
				case 'à':
					sb.append("a");
					break;
				case 'á':
					sb.append("b");
					break;
				case 'â':
					sb.append("v");
					break;
				case 'ã':
					sb.append("g");
					break;
				case 'ä':
					sb.append("d");
					break;
				case 'å':
					sb.append("e");
					break;
				case '¸':
					sb.append("yo");
					break;
				case 'æ':
					sb.append("zh");
					break;
				case 'ç':
					sb.append("z");
					break;
				case 'è':
					sb.append("i");
					break;
				case 'é':
					sb.append("i");
					break;
				case 'ê':
					sb.append("k");
					break;
				case 'ë':
					sb.append("l");
					break;
				case 'ì':
					sb.append("m");
					break;
				case 'í':
					sb.append("n");
					break;
				case 'î':
					sb.append("o");
					break;
				case 'ï':
					sb.append("p");
					break;
				case 'ð':
					sb.append("r");
					break;
				case 'ñ':
					sb.append("s");
					break;
				case 'ò':
					sb.append("t");
					break;
				case 'ó':
					sb.append("u");
					break;
				case 'ô':
					sb.append("f");
					break;
				case 'õ':
					sb.append("h");
					break;
				case 'ö':
					sb.append("c");
					break;
				case '÷':
					sb.append("ch");
					break;
				case 'ø':
					sb.append("sh");
					break;
				case 'ù':
					sb.append("sh");
					break;
				case 'ú':
					sb.append("'");
					break;
				case 'û':
					sb.append("i");
					break;
				case 'ü':
					sb.append("'");
					break;
				case 'ý':
					sb.append("e");
					break;
				case 'þ':
					sb.append("yu");
					break;
				case 'ÿ':
					sb.append("ya");
					break;

				case 'À':
					sb.append("A");
					break;
				case 'Á':
					sb.append("B");
					break;
				case 'Â':
					sb.append("V");
					break;
				case 'Ã':
					sb.append("G");
					break;
				case 'Ä':
					sb.append("D");
					break;
				case 'Å':
					sb.append("E");
					break;
				case '¨':
					sb.append("YO");
					break;
				case 'Æ':
					sb.append("ZH");
					break;
				case 'Ç':
					sb.append("Z");
					break;
				case 'È':
					sb.append("I");
					break;
				case 'É':
					sb.append("I");
					break;
				case 'Ê':
					sb.append("K");
					break;
				case 'Ë':
					sb.append("L");
					break;
				case 'Ì':
					sb.append("M");
					break;
				case 'Í':
					sb.append("N");
					break;
				case 'Î':
					sb.append("O");
					break;
				case 'Ï':
					sb.append("P");
					break;
				case 'Ð':
					sb.append("R");
					break;
				case 'Ñ':
					sb.append("S");
					break;
				case 'Ò':
					sb.append("T");
					break;
				case 'Ó':
					sb.append("U");
					break;
				case 'Ô':
					sb.append("F");
					break;
				case 'Õ':
					sb.append("H");
					break;
				case 'Ö':
					sb.append("C");
					break;
				case '×':
					sb.append("CH");
					break;
				case 'Ø':
					sb.append("SH");
					break;
				case 'Ù':
					sb.append("SH");
					break;
				case 'Ú':
					sb.append("'");
					break;
				case 'Û':
					sb.append("I");
					break;
				case 'Ü':
					sb.append("'");
					break;
				case 'Ý':
					sb.append("E");
					break;
				case 'Þ':
					sb.append("YU");
					break;
				case 'ß':
					sb.append("YA");
					break;

				default:
					sb.append(ch);
				}
			}
			return sb.toString();
		}
	}

	public static char Byte2Char(int i){
		if (i < 0) i += 256;
		char c = (char)i;
		if (c == '\n') return c;
		if (c == '\r') return c;
		if (c == '\t') return c;
		if (c < ' ') return ' ';
		if (c < '\200') return c;
		if (i == 168) return '\u0401';
		if (i == 184) return '\u0451';
		if (i == 179) return '\u0456';
		if (i == 178) return '\u0406';
		if (i == 191) return '\u0457';
		if (i == 175) return '\u0407';
		if (i == 186) return '\u0454';
		if (i == 170) return '\u0404';
		if (i >= 192 && i <= 255) return (char)(i + 848);
		return ' ';
	}

	public static int Char2Byte(char c){
		char c1 = c;
		if (c == '\n') return c;
		if (c == '\r') return c;
		if (c == '\t') return c;
		if (c < ' ')   return 32;
		if (c < '\200') return c;
		if (c1 == '\u0401') return 168;
		if (c1 == '\u0451') return 184;
		if (c1 == '\u0456') return 179;
		if (c1 == '\u0406') return 178;
		if (c1 == '\u0457') return 191;
		if (c1 == '\u0407') return 175;
		if (c1 == '\u0454') return 186;
		if (c1 == '\u0404') return 170;
		if (c1 >= '\u0410' && c1 <= '\u044F') return c1 - 848;
		return 32;
	}

	public static String Ascii2Utf(String s){
		String s1 = "";
		try {
			byte abyte0[] = s.getBytes();
		for (int i = 0; i < abyte0.length; i++) s1 = s1 + String.valueOf(Byte2Char(abyte0[i]));
		} catch (Exception exception){
			s1 = "";
		}
		return s1;
	}

	public static String Utf2Ascii(String s){
		String s1 = "";
		try {
			char ac[] = s.toCharArray();
			for(int i = 0; i < ac.length; i++) s1 = s1 + (char)Char2Byte(ac[i]);
		} catch (Exception exception){
			s1 = "";
		}
		return s1;
	}

}