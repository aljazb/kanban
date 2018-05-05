export class Color {

  static BLACK_HEX = '#000000';
  static WHITE_HEX = '#FFFFFF';
  static YELLOW = new Color('Yellow', '#FFFF00', Color.BLACK_HEX);
  static PINK = new Color('Pink', '#FF69B4', Color.BLACK_HEX);
  static ORANGE = new Color('Orange', '#FF8C00', Color.BLACK_HEX);
  static SILVER = new Color('Silver', '#C0C0C0', Color.BLACK_HEX);
  static PURPLE = new Color('Purple', '#9400D3', Color.BLACK_HEX);
  static GREEN = new Color('Green', '#32CD32', Color.BLACK_HEX);
  static BLUE = new Color('Blue', '#1E90FF', Color.WHITE_HEX);
  static BROWN = new Color('Brown', '#8B4513', Color.WHITE_HEX);
  static BLACK = new Color('Black', '#000000', Color.WHITE_HEX);
  static WHITE = new Color('White', '#FFFFFF', Color.BLACK_HEX);

  name: string;
  hexBackgroundColor: string;
  hexTextColor: string;


  constructor(name: string, hexBackgroundColor: string, hexTextColor: string) {
    this.name = name;
    this.hexBackgroundColor = hexBackgroundColor;
    this.hexTextColor = hexTextColor;
  }
}

export const COLOR_PALETTE: Color[] = [
  Color.YELLOW,
  Color.PINK,
  Color.ORANGE,
  Color.PURPLE,
  Color.GREEN,
  Color.BLACK,
  Color.BLUE,
  Color.BROWN,
  Color.WHITE
];

export function buildMap(): Map<string, Color> {
  let map: Map<string, Color> = new Map<string, Color>();
  COLOR_PALETTE.forEach(c => map.set(c.name, c));
  return map;
}

