<?xml version="1.0" encoding="UTF-8"?><sld:UserStyle xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
  <sld:Name>raster</sld:Name>
  <sld:Title>A very simple color map</sld:Title>
  <sld:Abstract>A very basic color map</sld:Abstract>
  <sld:FeatureTypeStyle>
    <sld:Name>name</sld:Name>
    <sld:FeatureTypeName>Feature</sld:FeatureTypeName>
    <sld:Rule>
      <sld:RasterSymbolizer>
        <sld:Geometry>
          <ogc:PropertyName>geom</ogc:PropertyName>
        </sld:Geometry>
        <sld:ChannelSelection>
          <sld:GrayChannel>
            <sld:SourceChannelName>1</sld:SourceChannelName>
          </sld:GrayChannel>
        </sld:ChannelSelection>
        <sld:ColorMap>
          <sld:ColorMapEntry color="#ffffff" opacity="0" quantity="-999"/>
          <sld:ColorMapEntry color="#002DD0" quantity="7.486259" label="7.486259 kgN ha-1"/>
          <sld:ColorMapEntry color="#005BA2" quantity="411.606"/>
          <sld:ColorMapEntry color="#008C73" quantity="599.7859"/>
          <sld:ColorMapEntry color="#00B944" quantity="769.2512"/>
          <sld:ColorMapEntry color="#00E716" quantity="968.104"/>
          <sld:ColorMapEntry color="#A0FF00" quantity="1279.886"/>
          <sld:ColorMapEntry color="#FFFF00" quantity="1777.572"/>
          <sld:ColorMapEntry color="#FFC814" quantity="2803.523"/>
          <sld:ColorMapEntry color="#FFA000" quantity="4471.807"/>
          <sld:ColorMapEntry color="#FF5B00" quantity="7455.45"/>
          <sld:ColorMapEntry color="#FF0000" quantity="101133.6" label="101133.6 kgN ha-1"/>
        </sld:ColorMap>
      </sld:RasterSymbolizer>
    </sld:Rule>
  </sld:FeatureTypeStyle>
</sld:UserStyle>